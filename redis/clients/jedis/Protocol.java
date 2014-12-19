package redis.clients.jedis;

import java.io.*;
import redis.clients.jedis.exceptions.*;
import redis.clients.util.*;
import java.util.*;

public final class Protocol
{
    private static final String ASK_RESPONSE = "ASK";
    private static final String MOVED_RESPONSE = "MOVED";
    private static final String CLUSTERDOWN_RESPONSE = "CLUSTERDOWN";
    public static final int DEFAULT_PORT = 6379;
    public static final int DEFAULT_SENTINEL_PORT = 26379;
    public static final int DEFAULT_TIMEOUT = 2000;
    public static final int DEFAULT_DATABASE = 0;
    public static final String CHARSET = "UTF-8";
    public static final byte DOLLAR_BYTE = 36;
    public static final byte ASTERISK_BYTE = 42;
    public static final byte PLUS_BYTE = 43;
    public static final byte MINUS_BYTE = 45;
    public static final byte COLON_BYTE = 58;
    public static final String SENTINEL_MASTERS = "masters";
    public static final String SENTINEL_GET_MASTER_ADDR_BY_NAME = "get-master-addr-by-name";
    public static final String SENTINEL_RESET = "reset";
    public static final String SENTINEL_SLAVES = "slaves";
    public static final String SENTINEL_FAILOVER = "failover";
    public static final String SENTINEL_MONITOR = "monitor";
    public static final String SENTINEL_REMOVE = "remove";
    public static final String SENTINEL_SET = "set";
    public static final String CLUSTER_NODES = "nodes";
    public static final String CLUSTER_MEET = "meet";
    public static final String CLUSTER_RESET = "reset";
    public static final String CLUSTER_ADDSLOTS = "addslots";
    public static final String CLUSTER_DELSLOTS = "delslots";
    public static final String CLUSTER_INFO = "info";
    public static final String CLUSTER_GETKEYSINSLOT = "getkeysinslot";
    public static final String CLUSTER_SETSLOT = "setslot";
    public static final String CLUSTER_SETSLOT_NODE = "node";
    public static final String CLUSTER_SETSLOT_MIGRATING = "migrating";
    public static final String CLUSTER_SETSLOT_IMPORTING = "importing";
    public static final String CLUSTER_SETSLOT_STABLE = "stable";
    public static final String CLUSTER_FORGET = "forget";
    public static final String CLUSTER_FLUSHSLOT = "flushslots";
    public static final String CLUSTER_KEYSLOT = "keyslot";
    public static final String CLUSTER_COUNTKEYINSLOT = "countkeysinslot";
    public static final String CLUSTER_SAVECONFIG = "saveconfig";
    public static final String CLUSTER_REPLICATE = "replicate";
    public static final String CLUSTER_SLAVES = "slaves";
    public static final String CLUSTER_FAILOVER = "failover";
    public static final String CLUSTER_SLOTS = "slots";
    public static final String PUBSUB_CHANNELS = "channels";
    public static final String PUBSUB_NUMSUB = "numsub";
    public static final String PUBSUB_NUM_PAT = "numpat";
    
    public static void sendCommand(final RedisOutputStream os, final Command command, final byte[]... args) {
        sendCommand(os, command.raw, args);
    }
    
    private static void sendCommand(final RedisOutputStream os, final byte[] command, final byte[]... args) {
        try {
            os.write((byte)42);
            os.writeIntCrLf(args.length + 1);
            os.write((byte)36);
            os.writeIntCrLf(command.length);
            os.write(command);
            os.writeCrLf();
            for (final byte[] arg : args) {
                os.write((byte)36);
                os.writeIntCrLf(arg.length);
                os.write(arg);
                os.writeCrLf();
            }
        }
        catch (IOException e) {
            throw new JedisConnectionException(e);
        }
    }
    
    private static void processError(final RedisInputStream is) {
        final String message = is.readLine();
        if (message.startsWith("MOVED")) {
            final String[] movedInfo = parseTargetHostAndSlot(message);
            throw new JedisMovedDataException(message, new HostAndPort(movedInfo[1], Integer.valueOf(movedInfo[2])), Integer.valueOf(movedInfo[0]));
        }
        if (message.startsWith("ASK")) {
            final String[] askInfo = parseTargetHostAndSlot(message);
            throw new JedisAskDataException(message, new HostAndPort(askInfo[1], Integer.valueOf(askInfo[2])), Integer.valueOf(askInfo[0]));
        }
        if (message.startsWith("CLUSTERDOWN")) {
            throw new JedisClusterException(message);
        }
        throw new JedisDataException(message);
    }
    
    private static String[] parseTargetHostAndSlot(final String clusterRedirectResponse) {
        final String[] response = new String[3];
        final String[] messageInfo = clusterRedirectResponse.split(" ");
        final String[] targetHostAndPort = messageInfo[2].split(":");
        response[0] = messageInfo[1];
        response[1] = targetHostAndPort[0];
        response[2] = targetHostAndPort[1];
        return response;
    }
    
    private static Object process(final RedisInputStream is) {
        try {
            final byte b = is.readByte();
            if (b == 45) {
                processError(is);
            }
            else {
                if (b == 42) {
                    return processMultiBulkReply(is);
                }
                if (b == 58) {
                    return processInteger(is);
                }
                if (b == 36) {
                    return processBulkReply(is);
                }
                if (b == 43) {
                    return processStatusCodeReply(is);
                }
                throw new JedisConnectionException("Unknown reply: " + (char)b);
            }
        }
        catch (IOException e) {
            throw new JedisConnectionException(e);
        }
        return null;
    }
    
    private static byte[] processStatusCodeReply(final RedisInputStream is) {
        return SafeEncoder.encode(is.readLine());
    }
    
    private static byte[] processBulkReply(final RedisInputStream is) {
        final int len = Integer.parseInt(is.readLine());
        if (len == -1) {
            return null;
        }
        final byte[] read = new byte[len];
        int offset = 0;
        try {
            while (offset < len) {
                final int size = is.read(read, offset, len - offset);
                if (size == -1) {
                    throw new JedisConnectionException("It seems like server has closed the connection.");
                }
                offset += size;
            }
            is.readByte();
            is.readByte();
        }
        catch (IOException e) {
            throw new JedisConnectionException(e);
        }
        return read;
    }
    
    private static Long processInteger(final RedisInputStream is) {
        final String num = is.readLine();
        return Long.valueOf(num);
    }
    
    private static List<Object> processMultiBulkReply(final RedisInputStream is) {
        final int num = Integer.parseInt(is.readLine());
        if (num == -1) {
            return null;
        }
        final List<Object> ret = new ArrayList<Object>(num);
        for (int i = 0; i < num; ++i) {
            try {
                ret.add(process(is));
            }
            catch (JedisDataException e) {
                ret.add(e);
            }
        }
        return ret;
    }
    
    public static Object read(final RedisInputStream is) {
        return process(is);
    }
    
    public static final byte[] toByteArray(final boolean value) {
        return toByteArray(value ? 1 : 0);
    }
    
    public static final byte[] toByteArray(final int value) {
        return SafeEncoder.encode(String.valueOf(value));
    }
    
    public static final byte[] toByteArray(final long value) {
        return SafeEncoder.encode(String.valueOf(value));
    }
    
    public static final byte[] toByteArray(final double value) {
        return SafeEncoder.encode(String.valueOf(value));
    }
    
    public enum Command
    {
        PING("PING", 0), 
        SET("SET", 1), 
        GET("GET", 2), 
        QUIT("QUIT", 3), 
        EXISTS("EXISTS", 4), 
        DEL("DEL", 5), 
        TYPE("TYPE", 6), 
        FLUSHDB("FLUSHDB", 7), 
        KEYS("KEYS", 8), 
        RANDOMKEY("RANDOMKEY", 9), 
        RENAME("RENAME", 10), 
        RENAMENX("RENAMENX", 11), 
        RENAMEX("RENAMEX", 12), 
        DBSIZE("DBSIZE", 13), 
        EXPIRE("EXPIRE", 14), 
        EXPIREAT("EXPIREAT", 15), 
        TTL("TTL", 16), 
        SELECT("SELECT", 17), 
        MOVE("MOVE", 18), 
        FLUSHALL("FLUSHALL", 19), 
        GETSET("GETSET", 20), 
        MGET("MGET", 21), 
        SETNX("SETNX", 22), 
        SETEX("SETEX", 23), 
        MSET("MSET", 24), 
        MSETNX("MSETNX", 25), 
        DECRBY("DECRBY", 26), 
        DECR("DECR", 27), 
        INCRBY("INCRBY", 28), 
        INCR("INCR", 29), 
        APPEND("APPEND", 30), 
        SUBSTR("SUBSTR", 31), 
        HSET("HSET", 32), 
        HGET("HGET", 33), 
        HSETNX("HSETNX", 34), 
        HMSET("HMSET", 35), 
        HMGET("HMGET", 36), 
        HINCRBY("HINCRBY", 37), 
        HEXISTS("HEXISTS", 38), 
        HDEL("HDEL", 39), 
        HLEN("HLEN", 40), 
        HKEYS("HKEYS", 41), 
        HVALS("HVALS", 42), 
        HGETALL("HGETALL", 43), 
        RPUSH("RPUSH", 44), 
        LPUSH("LPUSH", 45), 
        LLEN("LLEN", 46), 
        LRANGE("LRANGE", 47), 
        LTRIM("LTRIM", 48), 
        LINDEX("LINDEX", 49), 
        LSET("LSET", 50), 
        LREM("LREM", 51), 
        LPOP("LPOP", 52), 
        RPOP("RPOP", 53), 
        RPOPLPUSH("RPOPLPUSH", 54), 
        SADD("SADD", 55), 
        SMEMBERS("SMEMBERS", 56), 
        SREM("SREM", 57), 
        SPOP("SPOP", 58), 
        SMOVE("SMOVE", 59), 
        SCARD("SCARD", 60), 
        SISMEMBER("SISMEMBER", 61), 
        SINTER("SINTER", 62), 
        SINTERSTORE("SINTERSTORE", 63), 
        SUNION("SUNION", 64), 
        SUNIONSTORE("SUNIONSTORE", 65), 
        SDIFF("SDIFF", 66), 
        SDIFFSTORE("SDIFFSTORE", 67), 
        SRANDMEMBER("SRANDMEMBER", 68), 
        ZADD("ZADD", 69), 
        ZRANGE("ZRANGE", 70), 
        ZREM("ZREM", 71), 
        ZINCRBY("ZINCRBY", 72), 
        ZRANK("ZRANK", 73), 
        ZREVRANK("ZREVRANK", 74), 
        ZREVRANGE("ZREVRANGE", 75), 
        ZCARD("ZCARD", 76), 
        ZSCORE("ZSCORE", 77), 
        MULTI("MULTI", 78), 
        DISCARD("DISCARD", 79), 
        EXEC("EXEC", 80), 
        WATCH("WATCH", 81), 
        UNWATCH("UNWATCH", 82), 
        SORT("SORT", 83), 
        BLPOP("BLPOP", 84), 
        BRPOP("BRPOP", 85), 
        AUTH("AUTH", 86), 
        SUBSCRIBE("SUBSCRIBE", 87), 
        PUBLISH("PUBLISH", 88), 
        UNSUBSCRIBE("UNSUBSCRIBE", 89), 
        PSUBSCRIBE("PSUBSCRIBE", 90), 
        PUNSUBSCRIBE("PUNSUBSCRIBE", 91), 
        PUBSUB("PUBSUB", 92), 
        ZCOUNT("ZCOUNT", 93), 
        ZRANGEBYSCORE("ZRANGEBYSCORE", 94), 
        ZREVRANGEBYSCORE("ZREVRANGEBYSCORE", 95), 
        ZREMRANGEBYRANK("ZREMRANGEBYRANK", 96), 
        ZREMRANGEBYSCORE("ZREMRANGEBYSCORE", 97), 
        ZUNIONSTORE("ZUNIONSTORE", 98), 
        ZINTERSTORE("ZINTERSTORE", 99), 
        ZLEXCOUNT("ZLEXCOUNT", 100), 
        ZRANGEBYLEX("ZRANGEBYLEX", 101), 
        ZREMRANGEBYLEX("ZREMRANGEBYLEX", 102), 
        SAVE("SAVE", 103), 
        BGSAVE("BGSAVE", 104), 
        BGREWRITEAOF("BGREWRITEAOF", 105), 
        LASTSAVE("LASTSAVE", 106), 
        SHUTDOWN("SHUTDOWN", 107), 
        INFO("INFO", 108), 
        MONITOR("MONITOR", 109), 
        SLAVEOF("SLAVEOF", 110), 
        CONFIG("CONFIG", 111), 
        STRLEN("STRLEN", 112), 
        SYNC("SYNC", 113), 
        LPUSHX("LPUSHX", 114), 
        PERSIST("PERSIST", 115), 
        RPUSHX("RPUSHX", 116), 
        ECHO("ECHO", 117), 
        LINSERT("LINSERT", 118), 
        DEBUG("DEBUG", 119), 
        BRPOPLPUSH("BRPOPLPUSH", 120), 
        SETBIT("SETBIT", 121), 
        GETBIT("GETBIT", 122), 
        BITPOS("BITPOS", 123), 
        SETRANGE("SETRANGE", 124), 
        GETRANGE("GETRANGE", 125), 
        EVAL("EVAL", 126), 
        EVALSHA("EVALSHA", 127), 
        SCRIPT("SCRIPT", 128), 
        SLOWLOG("SLOWLOG", 129), 
        OBJECT("OBJECT", 130), 
        BITCOUNT("BITCOUNT", 131), 
        BITOP("BITOP", 132), 
        SENTINEL("SENTINEL", 133), 
        DUMP("DUMP", 134), 
        RESTORE("RESTORE", 135), 
        PEXPIRE("PEXPIRE", 136), 
        PEXPIREAT("PEXPIREAT", 137), 
        PTTL("PTTL", 138), 
        INCRBYFLOAT("INCRBYFLOAT", 139), 
        PSETEX("PSETEX", 140), 
        CLIENT("CLIENT", 141), 
        TIME("TIME", 142), 
        MIGRATE("MIGRATE", 143), 
        HINCRBYFLOAT("HINCRBYFLOAT", 144), 
        SCAN("SCAN", 145), 
        HSCAN("HSCAN", 146), 
        SSCAN("SSCAN", 147), 
        ZSCAN("ZSCAN", 148), 
        WAIT("WAIT", 149), 
        CLUSTER("CLUSTER", 150), 
        ASKING("ASKING", 151), 
        PFADD("PFADD", 152), 
        PFCOUNT("PFCOUNT", 153), 
        PFMERGE("PFMERGE", 154);
        
        public final byte[] raw;
        
        private Command(final String s, final int n) {
            this.raw = SafeEncoder.encode(this.name());
        }
    }
    
    public enum Keyword
    {
        AGGREGATE("AGGREGATE", 0), 
        ALPHA("ALPHA", 1), 
        ASC("ASC", 2), 
        BY("BY", 3), 
        DESC("DESC", 4), 
        GET("GET", 5), 
        LIMIT("LIMIT", 6), 
        MESSAGE("MESSAGE", 7), 
        NO("NO", 8), 
        NOSORT("NOSORT", 9), 
        PMESSAGE("PMESSAGE", 10), 
        PSUBSCRIBE("PSUBSCRIBE", 11), 
        PUNSUBSCRIBE("PUNSUBSCRIBE", 12), 
        OK("OK", 13), 
        ONE("ONE", 14), 
        QUEUED("QUEUED", 15), 
        SET("SET", 16), 
        STORE("STORE", 17), 
        SUBSCRIBE("SUBSCRIBE", 18), 
        UNSUBSCRIBE("UNSUBSCRIBE", 19), 
        WEIGHTS("WEIGHTS", 20), 
        WITHSCORES("WITHSCORES", 21), 
        RESETSTAT("RESETSTAT", 22), 
        RESET("RESET", 23), 
        FLUSH("FLUSH", 24), 
        EXISTS("EXISTS", 25), 
        LOAD("LOAD", 26), 
        KILL("KILL", 27), 
        LEN("LEN", 28), 
        REFCOUNT("REFCOUNT", 29), 
        ENCODING("ENCODING", 30), 
        IDLETIME("IDLETIME", 31), 
        AND("AND", 32), 
        OR("OR", 33), 
        XOR("XOR", 34), 
        NOT("NOT", 35), 
        GETNAME("GETNAME", 36), 
        SETNAME("SETNAME", 37), 
        LIST("LIST", 38), 
        MATCH("MATCH", 39), 
        COUNT("COUNT", 40);
        
        public final byte[] raw;
        
        private Keyword(final String s, final int n) {
            this.raw = SafeEncoder.encode(this.name().toLowerCase());
        }
    }
}

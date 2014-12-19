package redis.clients.util;

import java.io.*;
import redis.clients.jedis.exceptions.*;

public class RedisInputStream extends FilterInputStream
{
    protected final byte[] buf;
    protected int count;
    protected int limit;
    
    public RedisInputStream(final InputStream in, final int size) {
        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.buf = new byte[size];
    }
    
    public RedisInputStream(final InputStream in) {
        this(in, 8192);
    }
    
    public byte readByte() throws IOException {
        if (this.count == this.limit) {
            this.fill();
        }
        return this.buf[this.count++];
    }
    
    public String readLine() {
        final StringBuilder sb = new StringBuilder();
        try {
            while (true) {
                if (this.count == this.limit) {
                    this.fill();
                }
                if (this.limit == -1) {
                    break;
                }
                final int b = this.buf[this.count++];
                if (b == 13) {
                    if (this.count == this.limit) {
                        this.fill();
                    }
                    if (this.limit == -1) {
                        sb.append((char)b);
                        break;
                    }
                    final byte c = this.buf[this.count++];
                    if (c == 10) {
                        break;
                    }
                    sb.append((char)b);
                    sb.append((char)c);
                }
                else {
                    sb.append((char)b);
                }
            }
        }
        catch (IOException e) {
            throw new JedisConnectionException(e);
        }
        final String reply = sb.toString();
        if (reply.length() == 0) {
            throw new JedisConnectionException("It seems like server has closed the connection.");
        }
        return reply;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (this.count == this.limit) {
            this.fill();
            if (this.limit == -1) {
                return -1;
            }
        }
        final int length = Math.min(this.limit - this.count, len);
        System.arraycopy(this.buf, this.count, b, off, length);
        this.count += length;
        return length;
    }
    
    private void fill() throws IOException {
        this.limit = this.in.read(this.buf);
        this.count = 0;
    }
}

package code.BreakMC.avalon;

import redis.clients.jedis.*;

public class DatabaseManager
{
    public Jedis jc;
    
    public DatabaseManager() {
        super();
        (this.jc = new Jedis("localhost")).auth("trAdRApAw2sP834e4ReJAYemAhetrEcr");
        this.jc.connect();
    }
}

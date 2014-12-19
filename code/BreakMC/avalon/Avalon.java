package code.BreakMC.avalon;

import org.bukkit.plugin.java.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import code.BreakMC.avalon.events.*;
import org.bukkit.command.*;
import code.BreakMC.avalon.commands.*;
import org.bukkit.*;
import code.BreakMC.avalon.util.*;
import com.sk89q.worldguard.bukkit.*;

public class Avalon extends JavaPlugin
{
    private static Avalon instance;
    private DatabaseManager dm;
    private SpawnManager sm;
    private boolean isLocked;
    
    public Avalon() {
        super();
        this.isLocked = true;
    }
    
    public void onEnable() {
        Avalon.instance = this;
        this.dm = new DatabaseManager();
        this.sm = new SpawnManager();
        this.getServer().getPluginManager().registerEvents((Listener)new StrenghtFix(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new WorldEvents(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new MushroomCowEvents(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new MineEvents(), (Plugin)this);
        this.getCommand("test").setExecutor((CommandExecutor)new TestCommand());
        this.getCommand("invsee").setExecutor((CommandExecutor)new InvseeCommand());
        this.getCommand("gamemode").setExecutor((CommandExecutor)new GamemodeCommand());
        this.getCommand("clear").setExecutor((CommandExecutor)new ClearCommand());
        this.getCommand("speed").setExecutor((CommandExecutor)new SpeedCommand());
        this.getCommand("fly").setExecutor((CommandExecutor)new FlyCommand());
        this.getCommand("heal").setExecutor((CommandExecutor)new HealCommand());
        this.getCommand("feed").setExecutor((CommandExecutor)new FeedCommand());
        this.getCommand("lag").setExecutor((CommandExecutor)new LagCommand());
        this.getCommand("spawn").setExecutor((CommandExecutor)new SpawnCommand());
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new Lag(), 100L, 1L);
        Bukkit.getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                Avalon.access$0(Avalon.this, false);
            }
        }, 100L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getWorld("world").getTime() >= 13000L) {
                    Bukkit.getWorld("world").setTime(0L);
                }
            }
        }, 0L, 100L);
    }
    
    public static Avalon getInstance() {
        return Avalon.instance;
    }
    
    public DatabaseManager getDatabaseManager() {
        return this.dm;
    }
    
    public SpawnManager getSpawnManager() {
        return this.sm;
    }
    
    public Boolean isServerLocked() {
        return this.isLocked;
    }
    
    public WorldGuardPlugin getWorldGuard() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
            return null;
        }
        return (WorldGuardPlugin)plugin;
    }
    
    static /* synthetic */ void access$0(final Avalon avalon, final boolean isLocked) {
        avalon.isLocked = isLocked;
    }
}

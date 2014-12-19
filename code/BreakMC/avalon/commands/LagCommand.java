package code.BreakMC.avalon.commands;

import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import code.BreakMC.avalon.util.*;
import org.bukkit.*;

public class LagCommand implements CommandExecutor, Listener
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return false;
        }
        final Player p = (Player)sender;
        final double tps = Lag.getTPS();
        final double lag = Math.round((1.0 - tps / 20.0) * 100.0);
        p.sendMessage("§cServer Performance: " + this.getTPSColor() + String.format("%.2f", tps) + " §7[" + this.getLagColor() + String.format("%.2f", lag) + "% §cLag§7]");
        if (p.hasPermission("avalon.lag")) {
            float used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            used /= Runtime.getRuntime().totalMemory();
            used *= 100.0f;
            float free = Runtime.getRuntime().freeMemory();
            free /= Runtime.getRuntime().totalMemory();
            free *= 100.0f;
            p.sendMessage("§cWorld: §b" + Bukkit.getWorld("world").getLoadedChunks().length + " §cChunks, §b" + Bukkit.getWorld("world").getEntities().size() + " §cEntities");
            p.sendMessage("§cMemory Usage: §b" + String.format("%.2f", used));
            p.sendMessage("§cMemory Free: §b" + String.format("%.2f", free));
        }
        return false;
    }
    
    private ChatColor getTPSColor() {
        final double tps = Lag.getTPS();
        if (tps >= 16.0) {
            return ChatColor.GREEN;
        }
        if (tps >= 11.0) {
            return ChatColor.YELLOW;
        }
        if (tps >= 6.0) {
            return ChatColor.GOLD;
        }
        if (tps >= 1.0) {
            return ChatColor.DARK_RED;
        }
        return ChatColor.GREEN;
    }
    
    private ChatColor getLagColor() {
        final double tps = Lag.getTPS();
        final double lag = Math.round((1.0 - tps / 20.0) * 100.0);
        if (lag <= 25.0) {
            return ChatColor.GREEN;
        }
        if (lag <= 50.0) {
            return ChatColor.YELLOW;
        }
        if (lag <= 75.0) {
            return ChatColor.GOLD;
        }
        if (lag <= 100.0) {
            return ChatColor.DARK_RED;
        }
        return ChatColor.GREEN;
    }
}

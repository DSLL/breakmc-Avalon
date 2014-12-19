package code.BreakMC.avalon.commands;

import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import code.BreakMC.avalon.*;

public class SpawnCommand implements CommandExecutor, Listener
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return false;
        }
        final Player p = (Player)sender;
        if (!p.hasPermission("avalon.spawn")) {
            p.sendMessage("§fUnknown command. Type \"/help\" for help.");
            return false;
        }
        p.sendMessage("§aYou have teleported to spawn.");
        Avalon.getInstance().getSpawnManager().teleport(p, Avalon.getInstance().getSpawnManager().getSpawn());
        return false;
    }
}

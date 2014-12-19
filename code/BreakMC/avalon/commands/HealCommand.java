package code.BreakMC.avalon.commands;

import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class HealCommand implements CommandExecutor, Listener
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return false;
        }
        final Player p = (Player)sender;
        if (!p.hasPermission("avalon.heal")) {
            p.sendMessage("§fUnknown command. Type \"/help\" for help.");
            return false;
        }
        if (args.length == 0) {
            p.setHealth(20.0);
            p.setFoodLevel(20);
            p.sendMessage("§aYou have been healed.");
        }
        else {
            if (args.length != 1) {
                p.sendMessage("§cUsage: /heal <player>");
                return false;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                p.sendMessage("§cThat is not a valid player.");
                return false;
            }
            final Player target = Bukkit.getPlayer(args[0]);
            target.setFoodLevel(20);
            target.setHealth(20.0);
            target.sendMessage("§aYou have been healed.");
            p.sendMessage("§aYou have healed §6" + target.getName() + "§a.");
        }
        return false;
    }
}

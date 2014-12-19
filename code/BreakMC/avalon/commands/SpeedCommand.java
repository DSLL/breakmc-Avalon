package code.BreakMC.avalon.commands;

import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class SpeedCommand implements CommandExecutor, Listener
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return false;
        }
        final Player p = (Player)sender;
        if (!p.hasPermission("avalon.speed")) {
            p.sendMessage("§fUnknown command. Type \"/help\" for help.");
            return false;
        }
        if (args.length == 1) {
            if (p.isFlying()) {
                p.setFlySpeed(Float.parseFloat(args[0]));
                p.sendMessage("§aFly speed set to §b" + Float.parseFloat(args[0]));
            }
            else {
                p.setWalkSpeed(Float.parseFloat(args[0]));
                p.sendMessage("§bWalk speed set to §b" + Float.parseFloat(args[0]));
            }
        }
        return false;
    }
}

package code.BreakMC.avalon.commands;

import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import code.BreakMC.avalon.*;

public class ClearCommand implements CommandExecutor, Listener
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return false;
        }
        final Player p = (Player)sender;
        if (!p.hasPermission("avalon.gamemode")) {
            p.sendMessage("§fUnknown command. Type \"/help\" for help.");
            return false;
        }
        p.sendMessage("§aYour inventory has been cleared.");
        Avalon.getInstance().getSpawnManager().clearInventory(p);
        return false;
    }
}

package code.BreakMC.avalon.commands;

import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;

public class TestCommand implements CommandExecutor, Listener
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final boolean b = sender instanceof Player;
        return false;
    }
    
    public int getPing(final Player p) {
        return ((CraftPlayer)p).getHandle().ping;
    }
}

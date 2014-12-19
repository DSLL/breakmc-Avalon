package code.BreakMC.avalon.commands;

import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class GamemodeCommand implements CommandExecutor, Listener
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
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage("§aYour game mode has been set to §bSurvival§a.");
            }
            else {
                if (!args[0].equalsIgnoreCase("creative") && !args[0].equalsIgnoreCase("c") && !args[0].equalsIgnoreCase("1")) {
                    p.sendMessage("§cUsage: /gamemode [survival/creative] <player>");
                    return false;
                }
                p.setGameMode(GameMode.CREATIVE);
                p.sendMessage("§aYour game mode has been set to §bCreative§a.");
            }
        }
        else {
            if (args.length != 2) {
                p.sendMessage("§cUsage: /gamemode [survival/creative] <player>");
                return false;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                p.sendMessage("§6" + args[0] + " §cis not a valid player.");
                return false;
            }
            final Player target = Bukkit.getPlayer(args[0]);
            if (args[1].equalsIgnoreCase("survival") || args[1].equalsIgnoreCase("s") || args[1].equalsIgnoreCase("0")) {
                target.setGameMode(GameMode.SURVIVAL);
                target.sendMessage("§aYour game mode has been set to §bSurvival§a.");
                p.sendMessage("§6" + target.getName() + "'s §agame mode has been set to §bSurvival§a.");
            }
            else {
                if (!args[1].equalsIgnoreCase("creative") && !args[1].equalsIgnoreCase("c") && !args[1].equalsIgnoreCase("1")) {
                    p.sendMessage("§cUsage: /gamemode [survival/creative] <player>");
                    return false;
                }
                target.setGameMode(GameMode.CREATIVE);
                target.sendMessage("§aYour game mode has been set to §bCreative§a.");
                p.sendMessage("§6" + target.getName() + "'s §agame mode has been set to §bCreative§a.");
            }
        }
        return false;
    }
}

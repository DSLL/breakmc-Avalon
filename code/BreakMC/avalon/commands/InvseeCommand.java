package code.BreakMC.avalon.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import code.BreakMC.avalon.*;
import org.bukkit.*;
import code.BreakMC.avalon.util.*;
import org.bukkit.potion.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import org.bukkit.plugin.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.*;

public class InvseeCommand implements CommandExecutor, Listener
{
    public HashMap<UUID, Integer> TID;
    
    public InvseeCommand() {
        super();
        this.TID = new HashMap<UUID, Integer>();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return false;
        }
        final Player p = (Player)sender;
        if (!p.hasPermission("avalon.invsee")) {
            p.sendMessage("§fUnknown command. Type \"/help\" for help.");
            return false;
        }
        if (args.length != 1) {
            p.sendMessage("§cUsage: /invsee <player>");
            return false;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            p.sendMessage("That is not a valid player.");
            return false;
        }
        this.startInvsee(p, Bukkit.getPlayer(args[0]));
        return false;
    }
    
    public void startInvsee(final Player p, final Player target) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)p, 45, "§6§l" + target.getName());
        final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)Avalon.getInstance(), (Runnable)new Runnable() {
            @Override
            public void run() {
                if (target != null) {
                    inv.setContents(target.getInventory().getContents());
                    inv.setItem(36, target.getInventory().getHelmet());
                    inv.setItem(37, target.getInventory().getChestplate());
                    inv.setItem(38, target.getInventory().getLeggings());
                    inv.setItem(39, target.getInventory().getBoots());
                    inv.setItem(43, ItemUtill.createItem(Material.COOKED_BEEF, target.getFoodLevel(), "§6Hunger", "§b" + target.getFoodLevel()));
                    final ItemStack powder = ItemUtill.createItem(Material.NETHER_STALK, "§aPotion Effects");
                    final ItemMeta im = powder.getItemMeta();
                    final ArrayList<String> lore = new ArrayList<String>();
                    if (p.getActivePotionEffects().size() > 0) {
                        for (final PotionEffect pe : p.getActivePotionEffects()) {
                            lore.add("§b" + ItemUtill.getPotionName(pe.getType()) + " " + ItemUtill.getPotionAmplifier(pe) + "§7: " + InvseeCommand.convertSecondsToMinutes(ItemUtill.getPotionDuration(pe)));
                        }
                    }
                    else {
                        lore.add("§bNone");
                    }
                    im.setLore((List)lore);
                    powder.setItemMeta(im);
                    inv.setItem(44, powder);
                }
            }
        }, 0L, 3L);
        p.openInventory(inv);
        this.TID.put(p.getUniqueId(), id);
    }
    
    public void stopScheduler(final Player p) {
        if (this.TID.containsKey(p.getUniqueId())) {
            final int id = this.TID.get(p.getUniqueId());
            Bukkit.getScheduler().cancelTask(id);
            this.TID.remove(p.getUniqueId());
        }
    }
    
    @EventHandler
    public void onClose(final InventoryCloseEvent e) {
        if (this.TID.containsKey(e.getPlayer().getUniqueId())) {
            this.stopScheduler((Player)e.getPlayer());
        }
    }
    
    public static String convertSecondsToMinutes(final int time) {
        final int minutes = time / 60;
        final int seconds = time % 60;
        final String disMinu = new StringBuilder().append(minutes).toString();
        final String disSec = String.valueOf((seconds < 10) ? "0" : "") + seconds;
        final String formattedTime = String.valueOf(disMinu) + ":" + disSec;
        return formattedTime;
    }
}

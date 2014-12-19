package code.BreakMC.avalon.events;

import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.event.*;

public class MushroomCowEvents implements Listener
{
    @EventHandler
    public void onInteract(final PlayerInteractEntityEvent e) {
        final Player p = e.getPlayer();
        final Entity ent = e.getRightClicked();
        if (ent.getType() == EntityType.MUSHROOM_COW && p.getItemInHand().getType() == Material.GLASS_BOTTLE) {
            int count = 0;
            for (final ItemStack i : p.getInventory()) {
                if (i == null) {
                    ++count;
                }
            }
            if (count >= 1) {
                if (p.getItemInHand().getAmount() > 1) {
                    final ItemStack potion = new ItemStack(Material.POTION, 1);
                    potion.setDurability((short)8229);
                    p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                    p.getInventory().addItem(new ItemStack[] { potion });
                    p.updateInventory();
                }
                else {
                    final ItemStack potion = new ItemStack(Material.POTION, 1);
                    potion.setDurability((short)8229);
                    p.setItemInHand(potion);
                    p.updateInventory();
                }
            }
            else {
                final Location loc = p.getLocation();
                final ItemStack potion2 = new ItemStack(Material.POTION, 1);
                potion2.setDurability((short)8229);
                loc.getWorld().dropItemNaturally(loc, potion2);
                p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                p.updateInventory();
            }
        }
    }
}

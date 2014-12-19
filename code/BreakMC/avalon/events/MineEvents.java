package code.BreakMC.avalon.events;

import org.bukkit.event.block.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;

public class MineEvents implements Listener
{
    @EventHandler
    public void onMine(final BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.COAL_ORE) {
            final Random r = new Random();
            if (r.nextInt(100) >= 0 && r.nextInt(100) <= 29) {
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.SULPHUR, 1));
                return;
            }
        }
        if (e.getBlock().getType() == Material.SAND) {
            final Random r = new Random();
            if (r.nextInt(100) >= 0 && r.nextInt(100) <= 9) {
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.GLASS, 1));
            }
        }
    }
}

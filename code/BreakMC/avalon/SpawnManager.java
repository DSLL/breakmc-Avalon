package code.BreakMC.avalon;

import org.bukkit.entity.*;
import code.BreakMC.avalon.util.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import java.util.*;

public class SpawnManager
{
    Location spawn;
    Location spawnC1;
    Location spawnC2;
    Location kitAreaC1;
    Location kitAreaC2;
    ArrayList<UUID> inKitSelection;
    
    public SpawnManager() {
        super();
        this.spawn = new Location(Bukkit.getWorld("world"), 500.5, 10.0, 500.5);
        this.spawnC1 = new Location(Bukkit.getWorld("world"), 448.5, 0.0, 552.5);
        this.spawnC2 = new Location(Bukkit.getWorld("world"), 552.5, 256.0, 443.5);
        this.kitAreaC1 = new Location(Bukkit.getWorld("world"), 495.5, 0.0, 539.5);
        this.kitAreaC2 = new Location(Bukkit.getWorld("world"), 505.5, 256.0, 547.5);
        this.inKitSelection = new ArrayList<UUID>();
    }
    
    public void setSpawnInventory(final Player p) {
        this.clearInventory(p);
        this.managePlayer(p);
        p.getInventory().setItem(7, ItemUtill.createItem(Material.INK_SACK, (short)8, 1, "§aUnranked 1v1 Queue", "§7Right-Click to choose kit"));
        p.getInventory().setItem(8, ItemUtill.createItem(Material.INK_SACK, (short)10, 1, "§bRanked 1v1 Queue", "§7Right-Click to choose kit"));
        p.updateInventory();
    }
    
    public void clearInventory(final Player p) {
        final PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setHelmet((ItemStack)null);
        inv.setChestplate((ItemStack)null);
        inv.setLeggings((ItemStack)null);
        inv.setBoots((ItemStack)null);
        p.updateInventory();
    }
    
    public void managePlayer(final Player p) {
        for (final PotionEffect pe : p.getActivePotionEffects()) {
            p.removePotionEffect(pe.getType());
        }
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.setFallDistance(0.0f);
        p.setWalkSpeed(0.2f);
        p.setGameMode(GameMode.SURVIVAL);
        p.setExp(0.0f);
        p.setLevel(0);
    }
    
    public void teleport(final Player p, final Location l) {
        l.getChunk().load();
        p.teleport(l);
    }
    
    public Location getSpawn() {
        return this.spawn;
    }
    
    public List<UUID> getInKitSelection() {
        return this.inKitSelection;
    }
    
    public boolean isInSpawn(final Player p) {
        final int x1 = Math.min(this.spawnC1.getBlockX(), this.spawnC2.getBlockX());
        final int z1 = Math.min(this.spawnC1.getBlockZ(), this.spawnC2.getBlockZ());
        final int x2 = Math.max(this.spawnC1.getBlockX(), this.spawnC2.getBlockX());
        final int z2 = Math.max(this.spawnC1.getBlockZ(), this.spawnC2.getBlockZ());
        return p.getLocation().getX() >= x1 && p.getLocation().getX() <= x2 && p.getLocation().getZ() >= z1 && p.getLocation().getZ() <= z2;
    }
    
    public boolean isInKitArea(final Player p) {
        final int x1 = Math.min(this.kitAreaC1.getBlockX(), this.kitAreaC2.getBlockX());
        final int z1 = Math.min(this.kitAreaC1.getBlockZ(), this.kitAreaC2.getBlockZ());
        final int x2 = Math.max(this.kitAreaC1.getBlockX(), this.kitAreaC2.getBlockX());
        final int z2 = Math.max(this.kitAreaC1.getBlockZ(), this.kitAreaC2.getBlockZ());
        return p.getLocation().getX() >= x1 && p.getLocation().getX() <= x2 && p.getLocation().getZ() >= z1 && p.getLocation().getZ() <= z2;
    }
}

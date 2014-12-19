package code.BreakMC.avalon.util;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import org.bukkit.potion.*;

public class ItemUtill
{
    public static ItemStack createItem(final Material material, final String displayname) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createItem(final Material material, final int amount, final String displayname) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createItem(final Material material, final short data, final String displayname) {
        final ItemStack item = new ItemStack(material);
        item.setDurability(data);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createItem(final Material material, final String displayname, final String... lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        final ArrayList<String> Lore = new ArrayList<String>();
        for (final String loreString : lore) {
            Lore.add(loreString);
        }
        meta.setLore((List)Lore);
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createItem(final Material material, final int amount, final String displayname, final String... lore) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        final ArrayList<String> Lore = new ArrayList<String>();
        for (final String loreString : lore) {
            Lore.add(loreString);
        }
        meta.setLore((List)Lore);
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createItem(final Material material, final short data, final int amount, final String displayname, final String... lore) {
        final ItemStack item = new ItemStack(material, amount);
        item.setDurability(data);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        final ArrayList<String> Lore = new ArrayList<String>();
        for (final String loreString : lore) {
            Lore.add(loreString);
        }
        meta.setLore((List)Lore);
        item.setItemMeta(meta);
        return item;
    }
    
    public static String getPotionName(final PotionEffectType pet) {
        if (pet.equals((Object)PotionEffectType.SPEED)) {
            return "Speed";
        }
        if (pet.equals((Object)PotionEffectType.SLOW)) {
            return "Slowness";
        }
        if (pet.equals((Object)PotionEffectType.FAST_DIGGING)) {
            return "Haste";
        }
        if (pet.equals((Object)PotionEffectType.SLOW_DIGGING)) {
            return "Fatigue";
        }
        if (pet.equals((Object)PotionEffectType.INCREASE_DAMAGE)) {
            return "Strenght";
        }
        if (pet.equals((Object)PotionEffectType.JUMP)) {
            return "Jump Boost";
        }
        if (pet.equals((Object)PotionEffectType.CONFUSION)) {
            return "Confusion";
        }
        if (pet.equals((Object)PotionEffectType.REGENERATION)) {
            return "Regeneration";
        }
        if (pet.equals((Object)PotionEffectType.DAMAGE_RESISTANCE)) {
            return "Resistance";
        }
        if (pet.equals((Object)PotionEffectType.FIRE_RESISTANCE)) {
            return "Fire Resistance";
        }
        if (pet.equals((Object)PotionEffectType.WATER_BREATHING)) {
            return "Water Breathing";
        }
        if (pet.equals((Object)PotionEffectType.INVISIBILITY)) {
            return "Invisibility";
        }
        if (pet.equals((Object)PotionEffectType.BLINDNESS)) {
            return "Blindness";
        }
        if (pet.equals((Object)PotionEffectType.NIGHT_VISION)) {
            return "Night Vision";
        }
        if (pet.equals((Object)PotionEffectType.HUNGER)) {
            return "Hunger";
        }
        if (pet.equals((Object)PotionEffectType.WEAKNESS)) {
            return "Weakness";
        }
        if (pet.equals((Object)PotionEffectType.POISON)) {
            return "Poison";
        }
        if (pet.equals((Object)PotionEffectType.WITHER)) {
            return "Wither";
        }
        return null;
    }
    
    public static String getPotionAmplifier(final PotionEffect pe) {
        if (pe.getAmplifier() == 0) {
            return "I";
        }
        if (pe.getAmplifier() == 1) {
            return "II";
        }
        if (pe.getAmplifier() == 2) {
            return "III";
        }
        if (pe.getAmplifier() == 3) {
            return "IV";
        }
        if (pe.getAmplifier() == 4) {
            return "V";
        }
        return null;
    }
    
    public static int getPotionDuration(final PotionEffect pe) {
        return pe.getDuration() / 20;
    }
}

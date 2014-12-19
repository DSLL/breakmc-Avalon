package code.BreakMC.avalon.util;

import com.google.common.collect.*;
import org.bukkit.entity.*;

public class Cooldowns
{
    private static Table<String, String, Long> cooldowns;
    
    static {
        Cooldowns.cooldowns = (Table<String, String, Long>)HashBasedTable.create();
    }
    
    public static long getCooldown(final Player player, final String key) {
        return calculateRemainder((Long)Cooldowns.cooldowns.get((Object)player.getName(), (Object)key));
    }
    
    public static long setCooldown(final Player player, final String key, final long delay) {
        return calculateRemainder((Long)Cooldowns.cooldowns.put((Object)player.getName(), (Object)key, (Object)(System.currentTimeMillis() + delay)));
    }
    
    public static boolean tryCooldown(final Player player, final String key, final long delay) {
        if (getCooldown(player, key) <= 0L) {
            setCooldown(player, key, delay);
            return true;
        }
        return false;
    }
    
    public static void removeCooldowns(final Player player) {
        Cooldowns.cooldowns.row((Object)player.getName()).clear();
    }
    
    private static long calculateRemainder(final Long expireTime) {
        return (expireTime != null) ? (expireTime - System.currentTimeMillis()) : Long.MIN_VALUE;
    }
}

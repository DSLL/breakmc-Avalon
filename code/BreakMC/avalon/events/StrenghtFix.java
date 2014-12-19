package code.BreakMC.avalon.events;

import org.bukkit.entity.*;
import org.bukkit.potion.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import code.BreakMC.avalon.*;
import java.util.*;

public class StrenghtFix implements Listener
{
    @EventHandler
    public void onPlayerDamage(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            final Player player = (Player)event.getDamager();
            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                for (final PotionEffect effect : player.getActivePotionEffects()) {
                    if (effect.getType().equals((Object)PotionEffectType.INCREASE_DAMAGE)) {
                        final int level = effect.getAmplifier() + 1;
                        final double newDamage = event.getDamage(EntityDamageEvent.DamageModifier.BASE) / (level * 1.3 + 1.0) + 2 * level;
                        final double damagePercent = newDamage / event.getDamage(EntityDamageEvent.DamageModifier.BASE);
                        try {
                            event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, event.getDamage(EntityDamageEvent.DamageModifier.ARMOR) * damagePercent);
                        }
                        catch (Exception ex) {}
                        try {
                            event.setDamage(EntityDamageEvent.DamageModifier.MAGIC, event.getDamage(EntityDamageEvent.DamageModifier.MAGIC) * damagePercent);
                        }
                        catch (Exception ex2) {}
                        try {
                            event.setDamage(EntityDamageEvent.DamageModifier.RESISTANCE, event.getDamage(EntityDamageEvent.DamageModifier.RESISTANCE) * damagePercent);
                        }
                        catch (Exception ex3) {}
                        try {
                            event.setDamage(EntityDamageEvent.DamageModifier.BLOCKING, event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) * damagePercent);
                        }
                        catch (Exception ex4) {}
                        event.setDamage(EntityDamageEvent.DamageModifier.BASE, newDamage);
                        break;
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onHungerLoss(final FoodLevelChangeEvent e) {
        if (!Avalon.getInstance().getSpawnManager().isInSpawn((Player)e.getEntity()) && e.getFoodLevel() < ((Player)e.getEntity()).getFoodLevel() && new Random().nextInt(100) > 4) {
            e.setCancelled(true);
        }
    }
}

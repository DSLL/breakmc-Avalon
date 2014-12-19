package code.BreakMC.avalon.events;

import org.bukkit.event.weather.*;
import org.bukkit.event.*;

public class WorldEvents implements Listener
{
    @EventHandler
    public void weatherChange(final WeatherChangeEvent e) {
        e.setCancelled(true);
    }
}

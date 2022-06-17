package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.List;

@AllArgsConstructor
public class BlockFoodWorldListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        final HumanEntity entity = e.getEntity();
        final ConfigUtil config = plugin.getConfig();
        final List<String> foodWorlds = config.getStringList("Features.WorldOptions.FoodWorlds");
        if(foodWorlds == null) return;
        if(!foodWorlds.contains(entity.getWorld().getName())) return;

        e.setFoodLevel(20);
        e.setCancelled(true);
    }

}

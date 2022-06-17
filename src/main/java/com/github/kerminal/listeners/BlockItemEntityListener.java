package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;

@AllArgsConstructor
public class BlockItemEntityListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onSpawnEntity(CreatureSpawnEvent e) {
        final LivingEntity entity = e.getEntity();
        entity.setCanPickupItems(false);
    }
}

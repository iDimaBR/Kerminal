package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

@AllArgsConstructor
public class BlockPlantationDamageListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onJumpPlantation(EntityChangeBlockEvent e) {
        if (e.getBlock().getType() != Material.SOIL) return;
        e.setCancelled(true);
    }
}

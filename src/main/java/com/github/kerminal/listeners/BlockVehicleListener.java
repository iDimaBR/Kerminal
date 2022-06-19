package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

@AllArgsConstructor
public class BlockVehicleListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onVehicle(VehicleEnterEvent e) {
        final Entity entity = e.getEntered();
        if(entity.getType() != EntityType.PLAYER) return;

        Player player = (Player) entity;
        if(player.hasPermission("kerminal.vehicle.bypass")) return;

        e.setCancelled(true);
    }
}

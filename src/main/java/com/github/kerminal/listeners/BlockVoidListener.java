package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@AllArgsConstructor
public class BlockVoidListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onsign(EntityDamageEvent e) {
        final Entity entity = e.getEntity();
        if(entity.getType() != EntityType.PLAYER) return;
        final Player player = (Player) entity;

        if(!e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) return;
        if(player.getLocation().getBlockY() > 0) return;

        e.setCancelled(true);
        player.setFallDistance(0);
        player.teleport(plugin.getSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}

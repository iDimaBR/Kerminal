package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@AllArgsConstructor
public class BlockFreezeListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onFreeze(BlockFormEvent e) {
        if(!e.getBlock().getType().toString().contains("WATER")) return;
        e.setCancelled(true);
    }
}

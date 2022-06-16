package com.github.kerminal.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


public class PlayerKeepExperienceListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        final Player player = e.getEntity().getPlayer();
        if (!player.hasPermission("kerminal.keepxp")) return;
        e.setKeepLevel(true);
        e.setDroppedExp(0);
    }
}

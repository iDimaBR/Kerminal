package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.world.PortalCreateEvent;

@AllArgsConstructor
public class BlockPortalListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onPortalCreated(PortalCreateEvent e) {
        e.setCancelled(true);
    }
}

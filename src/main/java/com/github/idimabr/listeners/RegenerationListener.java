package com.github.idimabr.listeners;

import com.github.idimabr.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

@AllArgsConstructor
public class RegenerationListener implements Listener {

    @EventHandler
    public void InventoryClick(EntityRegainHealthEvent e) {
        if(e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.CUSTOM))
            e.setCancelled(true);
    }
}

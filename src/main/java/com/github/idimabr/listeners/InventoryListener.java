package com.github.idimabr.listeners;

import com.github.idimabr.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class InventoryListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void InventoryClick(org.bukkit.event.inventory.InventoryClickEvent e) {
        if(e.getInventory().getName().equals("§aAjuda")) {
            e.setCancelled(true);
        }
    }
}

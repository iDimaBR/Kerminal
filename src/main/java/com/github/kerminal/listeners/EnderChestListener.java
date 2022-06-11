package com.github.kerminal.listeners;

import com.github.kerminal.commands.EnderChestCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class EnderChestListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onClickEnderchest(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        if (inventory.getType() != InventoryType.ENDER_CHEST) return;

        Player player = (Player) e.getWhoClicked();
        if (EnderChestCommand.enderchests.containsKey(player.getName()) && !(player.hasPermission("kerminal.enderchest.others"))) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }

    @EventHandler
    public void onCloseEnderchest(InventoryCloseEvent e){
        Inventory inventory = e.getInventory();
        if (inventory.getType() != InventoryType.ENDER_CHEST) return;

        Player player = (Player) e.getPlayer();
        if (EnderChestCommand.enderchests.containsKey(player.getName()) && !(player.hasPermission("kerminal.enderchest.others"))) {
            EnderChestCommand.enderchests.remove(player.getName());
        }
    }
}

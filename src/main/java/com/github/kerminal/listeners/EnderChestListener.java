package com.github.kerminal.listeners;

import com.github.kerminal.commands.EnderChestCommand;
import com.github.kerminal.utils.Utils;
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCloseEnderchest(InventoryCloseEvent e){
        final Inventory inventory = e.getInventory();
        final Player player = (Player) e.getPlayer();

        if(inventory.getType() != InventoryType.ENDER_CHEST) return;
        if(!player.hasPermission("kerminal.enderchest.others")) return;
        if(EnderChestCommand.enderchests.get(player.getName()) == null) return;

        Player target = Utils.getPlayer(EnderChestCommand.enderchests.get(player.getName()));
        if(target == null) return;
        EnderChestCommand.enderchests.remove(player.getName());
        if(target.isOnline()) return;

        target.getEnderChest().setContents(inventory.getContents());
        target.saveData();
    }
}

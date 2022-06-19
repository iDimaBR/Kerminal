package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AnvilColorListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onColorAnvil(InventoryClickEvent e) {
        final Player player = (Player) e.getWhoClicked();
        if (e.getInventory().getType() != InventoryType.ANVIL) return;
        if(e.getSlotType() != InventoryType.SlotType.RESULT) return;

        final ItemStack item = e.getCurrentItem();
        if(item == null) return;
        if(!item.getItemMeta().hasDisplayName()) return;

        final ItemMeta meta = item.getItemMeta();
        final String actualName = meta.getDisplayName();

        final ItemStack before = e.getInventory().getItem(0);
        if(before == null) return;

        final ItemMeta beforeMeta = before.getItemMeta();
        if(beforeMeta.hasDisplayName() && actualName.equals(beforeMeta.getDisplayName().replace("§", ""))){
            meta.setDisplayName(beforeMeta.getDisplayName());
            item.setItemMeta(meta);
            e.setCurrentItem(item);
            return;
        }

        if(!player.hasPermission("kerminal.anvil.color")) return;
        meta.setDisplayName(actualName.replace("&","§"));
        item.setItemMeta(meta);
        e.setCurrentItem(item);
    }
}

package com.github.kerminal.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

public class RepairUtils {
    public static void repairAll(Player p) {
        final ItemStack[] contents = p.getInventory().getContents();
        for (ItemStack item : contents) {
            if(item == null) continue;
            if(item.getType().getMaxDurability() == 0) continue;
            if(item.getDurability() == 0) continue;

            item.setDurability((short) 0);
        }

    }
    public static void repairHand(Player p) {
        if (p.getInventory().getItemInHand().getType() != null) {
            p.getInventory().getItemInHand().setDurability((short) 0);
        }
    }
}

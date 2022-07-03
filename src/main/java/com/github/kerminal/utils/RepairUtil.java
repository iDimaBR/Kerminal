package com.github.kerminal.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.Arrays;

public class RepairUtil {

    public static void repair(ItemStack itemStack) {
        if (itemStack == null) return;
        if (!(itemStack instanceof Repairable)) return;
        if (itemStack.getDurability() <= 0) return;

        itemStack.setDurability((short) 0);
    }

    public static void repairAll(Inventory inventory) {
        Arrays.stream(inventory.getContents()).forEach(RepairUtil::repair);
    }
}

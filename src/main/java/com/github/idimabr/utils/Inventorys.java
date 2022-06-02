package com.github.idimabr.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class Inventorys {

    public static Inventory getHelpInventory () {
        Inventory helpInventory = Bukkit.createInventory(null, 54, "Ajuda");

        ItemBuilder builder = new ItemBuilder(Material.POTATO);
        builder.setName("§a§lComandos");

        ItemBuilder builder1 = new ItemBuilder(Material.PAPER);
        builder1.setName("§a§lPermissões");

        helpInventory.setItem(1, builder.toItemStack());
        helpInventory.setItem(2, builder1.toItemStack());

        return helpInventory;
    }
}
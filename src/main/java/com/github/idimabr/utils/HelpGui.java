package com.github.idimabr.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class HelpGui {

    public static Inventory getHelpInventory () {
        Inventory helpInventory = Bukkit.createInventory(null, 54, "§aAjuda");

        ItemBuilder builder = new ItemBuilder(Material.POTATO);
        builder.setName("§a§lComandos");
        builder.setLore(
                "§2 Clique aqui para ver todos comandos!"
        );
        ItemBuilder builder1 = new ItemBuilder(Material.PAPER);
        builder1.setName("§a§lPermissões");
        builder.setLore(
                "§2 Clique aqui ver todas permissões!"
        );

        helpInventory.setItem(30, builder.toItemStack());
        helpInventory.setItem(32, builder1.toItemStack());

        // TODO: CREATE THE GUI

        return helpInventory;
    }

    public static Inventory getCommandsInventory () {
        Inventory cInventory = Bukkit.createInventory(null, 54, "§aComandos");

        ItemBuilder builder = new ItemBuilder(Material.PAPER);
        builder.setName("§a§lComandos Gerais");

        return cInventory;
        // TODO: CREATE THE GUI
    }
}
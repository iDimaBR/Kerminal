package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.List;


@AllArgsConstructor
public class BlockCraftListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        Player player = (Player) e.getView().getPlayer();
        if(player.hasPermission("kerminal.blockcraft.bypass")) return;

        final Recipe recipe = e.getRecipe();
        if(recipe == null) return;
        if(recipe.getResult() == null) return;

        final ConfigUtil config = plugin.getConfig();
        final List<String> blockCrafts = config.getStringList("Crafts.Blacklist");

        final Material material = recipe.getResult().getType();
        if(blockCrafts.contains(material.name()))
            e.getInventory().setResult(new ItemStack(Material.AIR));
    }
}

package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.List;


@AllArgsConstructor
public class BlockNaturalSpawnListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onSpawnNaturally(CreatureSpawnEvent e) {
        CreatureSpawnEvent.SpawnReason reason = e.getSpawnReason();
        switch(reason){
            case NATURAL:
            case CHUNK_GEN:
            case JOCKEY:
            case MOUNT:
                e.setCancelled(true);
                break;
        }
    }
}

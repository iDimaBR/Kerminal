package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

@AllArgsConstructor
public class BlockFireListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onSpreadFire(BlockIgniteEvent e) {
        switch(e.getCause()){
            case LAVA:
            case SPREAD:
                e.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onBlockFire(BlockBurnEvent e) {
        e.setCancelled(true);
    }

}

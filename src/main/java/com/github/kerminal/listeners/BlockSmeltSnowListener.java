package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.world.PortalCreateEvent;

@AllArgsConstructor
public class BlockSmeltSnowListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onSmeltSnow(BlockFadeEvent e) {
        final Material type = e.getBlock().getType();

        if (type == Material.ICE || type == Material.SNOW)
            e.setCancelled(true);
    }
}

package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.LeavesDecayEvent;

@AllArgsConstructor
public class BlockDecayListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onLeaves(LeavesDecayEvent e) {
        e.setCancelled(true);
    }

}

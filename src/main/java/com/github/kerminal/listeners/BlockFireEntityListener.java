package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;

@AllArgsConstructor
public class BlockFireEntityListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onFireEntity(EntityCombustEvent e) {
        if (!(e instanceof EntityCombustByEntityEvent) && !(e instanceof EntityCombustByBlockEvent))
            e.setCancelled(true);
    }
}

package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@AllArgsConstructor
public class BlockExplodeItemListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onItemExplode(EntityDamageByEntityEvent e) {
        final Entity entity = e.getEntity();

        if (entity instanceof Item)
            e.setCancelled(true);
    }
}

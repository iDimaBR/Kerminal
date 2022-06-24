package com.github.kerminal.listeners;


import com.github.kerminal.Kerminal;
import com.github.kerminal.customevents.GamemodeChangeEvent;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.Material;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;

@AllArgsConstructor
public class ExplosionRepulsionListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if  (plugin.getConfig().getBoolean("ExplosionRepulsion.enabled")) {
            if (e.getEntity() instanceof org.bukkit.entity.TNTPrimed)
                for (Entity entity : e.getEntity().getWorld().getEntities()) {
                    if (entity.getLocation().distance(e.getEntity().getLocation()) < 10.0D) {
                        Vector v = entity.getLocation().add(0.0D, 1.0D, 0.0D).toVector().subtract(e.getEntity().getLocation().toVector());
                        double l = v.length();
                        v.normalize();
                        v.multiply(plugin.getConfig().getDouble("ExplosionRepulsion.multiply") / l);
                        if (entity instanceof Player) {
                            if (((Player) entity).getGameMode() == GameMode.SURVIVAL || ((Player) entity).getGameMode() == GameMode.ADVENTURE) {
                                entity.setVelocity(entity.getVelocity().add(v.divide(new Vector(1, 7, 1))));
                                continue;
                            }
                            entity.setVelocity(entity.getVelocity().add(v.divide(new Vector(3, 10, 3))));
                        }
                    }
                }
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if  (plugin.getConfig().getBoolean("ExplosionRepulsion.NoDamage")) {
            if (e.getEntity() instanceof org.bukkit.entity.Player && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
                e.setDamage(0.0D);
        }
    }
}


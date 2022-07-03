package com.github.kerminal.listeners;


import com.github.kerminal.Kerminal;
import com.github.kerminal.customevents.GamemodeChangeEvent;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.Material;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ExplosionRepulsionListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        final ConfigUtil config = plugin.getConfig();
        final Location location = e.getLocation();
        switch(e.getEntity().getType()){
            case PRIMED_TNT:
            case FIREBALL:
                for (Player radiusPlayer : getRadiusPlayers(location, 10)) {
                    Vector vector = radiusPlayer.getLocation()
                            .add(0.0D, 1.0D, 0.0D)
                            .toVector()
                            .subtract(location.toVector())
                            .normalize();
                    vector = vector.multiply(
                        config.getDouble("ExplosionRepulse.Repulsion") / vector.length()
                    );
                    radiusPlayer.setVelocity(
                            radiusPlayer.getVelocity().add(
                                    vector.divide(new Vector(3, 10, 3))
                            )
                    );
                }
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (plugin.getConfig().getBoolean("ExplosionRepulse.RemoveDamage")) return;
        if (e.getEntity().getType() == EntityType.PLAYER && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
            e.setDamage(0.0D);
    }

    private List<Player> getRadiusPlayers(Location location, int radius){
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter($ -> $.getLocation().distance(location) < radius)
                .collect(Collectors.toList());
    }
}


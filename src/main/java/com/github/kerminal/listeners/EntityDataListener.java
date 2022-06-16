package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.EntityController;
import com.github.kerminal.models.EntityData;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EntityDataListener implements Listener {

    private final Kerminal plugin;

    public EntityDataListener(Kerminal plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDataEntityDeath(EntityDeathEvent e){
        final EntityController controller = plugin.getEntityController();
        final LivingEntity entity = e.getEntity();
        final EntityType entityType = entity.getType();

        final HashMap<EntityType, EntityData> dataEntityMap = controller.getDataEntityMap();
        if(dataEntityMap.get(entityType) == null) return;

        e.getDrops().clear();

        final EntityData data = dataEntityMap.get(entityType);
        for (Map.Entry<ItemStack, Integer> entry : data.getDrops().entrySet()) {
            final ItemStack item = entry.getKey();
            final Integer chance = entry.getValue();
            if(RandomUtils.nextInt(100) > chance) continue;

            entity.getWorld().dropItemNaturally(entity.getLocation(), item);
        }
    }

    @EventHandler
    public void onDataEntitySpawn(EntitySpawnEvent e){
        final EntityController controller = plugin.getEntityController();
        final Entity entity = e.getEntity();
        final EntityType entityType = entity.getType();

        final HashMap<EntityType, EntityData> dataEntityMap = controller.getDataEntityMap();
        if(dataEntityMap.get(entityType) == null) return;
        if(!(entity instanceof LivingEntity)) return;

        final LivingEntity livingEntity = (LivingEntity) entity;

        final EntityData data = dataEntityMap.get(entityType);
        final int newHealth = data.getHealth();
        livingEntity.setMaxHealth(newHealth);
        livingEntity.setHealth(newHealth);
    }
}

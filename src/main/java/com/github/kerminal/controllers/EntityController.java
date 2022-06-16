package com.github.kerminal.controllers;

import com.github.kerminal.Kerminal;
import com.github.kerminal.models.EntityData;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.ItemBuilder;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class EntityController {

    private final Kerminal plugin;
    private final ConfigUtil entities;

    private HashMap<EntityType, EntityData> dataEntityMap = new HashMap<>();

    public void loadEntityData(){
        for (String entityName : entities.getKeys(false)) {
            EntityType type = EntityType.fromName(entityName);
            if(type == null){
                Bukkit.getLogger().warning("A entidade '" + entityName + "' não foi encontrada em entities.yml");
                continue;
            }

            final EntityData data = new EntityData();

            final ConfigurationSection entitySection = entities.getConfigurationSection(type.name());
            final int health = entitySection.getInt("Health");

            final Map<ItemStack, Integer> drops = new HashMap<>();
            final ConfigurationSection mainDrops = entitySection.getConfigurationSection("Drops");
            for (String dropKey : mainDrops.getKeys(false)) {
                final ConfigurationSection dropSection = mainDrops.getConfigurationSection(dropKey);

                final Material material = Material.getMaterial(dropSection.getString("material"));
                if(material == null) continue;


                int chance = 100;
                if(dropSection.contains("chance"))
                    chance = dropSection.getInt("chance");

                ItemBuilder builder = new ItemBuilder(material);

                if(dropSection.contains("data"))
                    builder.setDurability((short) dropSection.getInt("data"));

                if(dropSection.contains("quantity"))
                    builder.setQuantity(dropSection.getInt("quantity"));

                if(dropSection.contains("name"))
                    builder.setName(dropSection.getString("name").replace("&","§"));


                if(dropSection.contains("lore"))
                    builder.setLore(
                            dropSection.getStringList("Lore")
                                    .stream().map($ -> $.replace("&","§")).collect(Collectors.toList())
                    );

                if(dropSection.contains("enchantments")) {
                    for (String valueEnchant : dropSection.getStringList("enchantments")) {
                        if(!valueEnchant.contains(";")) continue;

                        final String nameEnchant = valueEnchant.split(";")[0];
                        final int levelEnchant = Integer.parseInt(valueEnchant.split(";")[1]);

                        final Enchantment ench = Enchantment.getByName(nameEnchant);
                        if(ench == null) continue;

                        builder.addUnsafeEnchantment(ench, levelEnchant);
                    }
                }

                ItemStack finalItem = builder.build();

                drops.put(finalItem, chance);
            }

            data.setHealth(health);
            data.setDrops(drops);

            dataEntityMap.put(type, data);
        }
    }
}

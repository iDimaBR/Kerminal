package com.github.kerminal.controllers;

import com.github.kerminal.Kerminal;
import com.github.kerminal.models.EntityData;
import com.github.kerminal.models.Kit;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.ItemBuilder;
import com.github.kerminal.utils.Serializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
public class KitController {

    private final Kerminal plugin;
    private final ConfigUtil kits;

    private HashMap<String, Kit> loadedKits = new HashMap<>();

    @SneakyThrows
    public void loadAllKits(){
        loadedKits.clear();
        for (String kitName : kits.getKeys(false)) {
            registerKit(kitName);
        }
    }

    public boolean existsKit(String name){
        return loadedKits.get(name) != null;
    }

    public Kit getKit(String name){
        return loadedKits.get(name);
    }

    @SneakyThrows
    public void registerKit(String name){
        final ItemStack[] kitItens = Serializer.itemStackArrayFromBase64(kits.getString(name + ".itens"));
        int delay = kits.getInt(name + ".delay");

        loadedKits.put(name, new Kit(name, kitItens, delay));
        Bukkit.getLogger().info("Kit '" + name + "' foi carregado.");
    }

    public void removeKit(String name){
        if(!existsKit(name)) return;
        kits.set(name, null);
        kits.save();
        loadedKits.remove(name);
    }
}

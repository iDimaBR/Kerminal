package com.github.kerminal.models;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
@RequiredArgsConstructor
public class Kit {

    private final String name;
    private final ItemStack[] itens;
    private final int delay;

    private Map<UUID, Long> cooldownMap = Maps.newHashMap();

    public int getNeedSlots(){
        return (int) Arrays.stream(itens)
                .filter($ -> $ != null && $.getType() != Material.AIR).count();
    }

}

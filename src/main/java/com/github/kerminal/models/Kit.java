package com.github.kerminal.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
@AllArgsConstructor
public class Kit {

    private String name;
    private ItemStack[] itens;
    private int delay;

    public int getNeedSlots(){
        return (int) Arrays.stream(itens)
                .filter($ -> $ != null && $.getType() != Material.AIR).count();
    }

}

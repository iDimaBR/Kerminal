package com.github.kerminal.models;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class EntityData {

    private Map<ItemStack, Integer> drops = new HashMap<>();

    private int health;

}

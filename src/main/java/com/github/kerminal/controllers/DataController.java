package com.github.kerminal.controllers;

import com.github.kerminal.models.PlayerData;
import com.github.kerminal.registry.TeleportRegistry;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class DataController {

    private final HashMap<UUID, PlayerData> DATALIST = Maps.newHashMap();
    private final TeleportRegistry registry = new TeleportRegistry();

    public PlayerData getDataPlayer(UUID uuid){
        return DATALIST.get(uuid);
    }
}

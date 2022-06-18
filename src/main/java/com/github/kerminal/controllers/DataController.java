package com.github.kerminal.controllers;

import com.github.kerminal.Kerminal;
import com.github.kerminal.models.PlayerData;
import com.github.kerminal.registry.TeleportRegistry;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class DataController {

    private final Kerminal plugin;
    private final TeleportRegistry registry;

    private HashMap<UUID, PlayerData> DATALIST = Maps.newHashMap();

    public PlayerData getDataPlayer(UUID uuid){
        return createData(uuid);
    }

    public PlayerData createData(UUID uuid){
        if(DATALIST.get(uuid) == null) {
            final PlayerData data = new PlayerData(uuid);
            DATALIST.put(uuid, data);
            return data;
        }else{
            return DATALIST.get(uuid);
        }
    }

    public TeleportRegistry getRegistryTeleport(){
        return registry;
    }
}

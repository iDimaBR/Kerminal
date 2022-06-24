package com.github.kerminal.controllers;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class LangController {

    private final Kerminal plugin;

    private Map<String, Object> languageMap = Maps.newHashMap();

    public void loadAllLangs(){
        languageMap.clear();
        final ConfigUtil messages = plugin.getMessages();
        for (String key : messages.getKeys(true)) {
            if(messages.isConfigurationSection(key)) continue;
            final Object value = messages.get(key);
            languageMap.put(key, value);
        }
        System.out.println("Foram carregadas " + languageMap.size() + " mensagens.");
    }

    public String getString(String key){
        return ((String) languageMap.get(key)).replace("&","§");
    }

    public boolean getBoolean(String key){
        return (Boolean) languageMap.get(key);
    }

    public List<String> getStringList(String key) {
        return ((List<String>) languageMap.get(key)).stream().map(
                $ -> $.replace("&","§")
        ).collect(Collectors.toList());
    }
}

package com.github.kerminal.utils;


import org.bukkit.GameMode;

import java.util.Arrays;
import java.util.List;

public enum Mode {
    SURVIVAL(GameMode.SURVIVAL, "survival", "0", "sobrevivencia"),
    CRIATIVO(GameMode.CREATIVE, "creative", "1", "criativo"),
    AVENTURA(GameMode.ADVENTURE, "adventure", "2", "aventura"),
    ESPECTADOR(GameMode.SPECTATOR, "spectator", "3", "espectador");

    private final GameMode gameMode;

    private final String[] args;

    Mode(GameMode gameMode, String... args) {
        this.gameMode = gameMode;
        this.args = args;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public List<String> asList() {
        return Arrays.asList(this.args);
    }

    public static Mode of(String name) {
        return Arrays.stream(values()).filter(mode -> mode.asList().contains(name)).findFirst().orElse(null);
    }
}

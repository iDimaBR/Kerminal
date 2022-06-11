package com.github.kerminal.utils;

import org.bukkit.enchantments.Enchantment;
import java.util.Arrays;
import java.util.List;

public enum Enchants {

    protecao(Enchantment.PROTECTION_ENVIRONMENTAL, "protecao", "protection", "proteção"),
    afiacao(Enchantment.DAMAGE_ALL, "afiação", "damage", "dano", "afiacao", "damage_all"),
    fogo(Enchantment.FIRE_ASPECT, "fogo", "fire", "fire_aspect"),
    inquebravel(Enchantment.DURABILITY, "inquebravel", "mending", "mendigamento", "durability");

    //TODO: PUT ALL PLZ
    private final Enchantment enchant;

    private final String[] args;

    Enchants(Enchantment enchant, String... args) {
        this.enchant = enchant;
        this.args = args;
    }

    public Enchantment getEnchantment() {
        return enchant;
    }

    public List<String> asList() {
        return Arrays.asList(this.args);
    }

    public static Enchants of(String name) {
        return Arrays.stream(values()).filter(mode -> mode.asList().contains(name)).findFirst().orElse(null);
    }
}

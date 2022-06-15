package com.github.kerminal.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {

    public static Location getLocationFromConfig(ConfigUtil config, String path){
        try {
            return new Location(
                    Bukkit.getWorld(config.getString(path + ".world")),
                    config.getDouble(path + ".x"),
                    config.getDouble(path + ".y"),
                    config.getDouble(path + ".z"));
        } catch (Throwable e) {
            return null;
        }
    }

    public static void setLocationConfig(ConfigUtil config, Location location, String path){
        try {
            config.set(path + ".world", location.getWorld().getName());
            config.set(path + ".x", location.getBlockX());
            config.set(path + ".y", location.getY());
            config.set(path + ".z", location.getBlockZ());
            config.save();
        } catch (Throwable ignored){}
    }
}

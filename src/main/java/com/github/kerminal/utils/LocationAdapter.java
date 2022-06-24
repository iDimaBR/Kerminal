package com.github.kerminal.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationAdapter {

    public static String toString(Location location) {
        if (location == null) return "none";

        final World world = location.getWorld();
        if (world == null) return "none";

        return world.getName() + ";" +
                location.getBlockX() + ";" +
                location.getBlockY() + ";" +
                location.getBlockZ() + ";" +
                location.getYaw() + ";" +
                location.getPitch();
    }

    public static Location toLocation(String path) {
        if (path == null || path.equalsIgnoreCase("none")) return null;

        String[] split = path.split(";");
        return new Location(
                Bukkit.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5])
        );
    }
}
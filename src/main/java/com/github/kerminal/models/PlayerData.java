package com.github.kerminal.models;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private transient UUID uuid;
    private HashMap<String, Home> homes;
    private Home defaultHome;
    private transient Location lastLocation;

    public PlayerData(UUID uuid, HashMap<String, Home> homes, Home defaultHome, Location lastLocation) {
        this.uuid = uuid;
        this.homes = homes;
        this.defaultHome = defaultHome;
        this.lastLocation = lastLocation;
    }

    public PlayerData(UUID uuid, Home defaultHome, Location lastLocation) {
        this.uuid = uuid;
        this.homes = new HashMap<>();
        this.defaultHome = defaultHome;
        this.lastLocation = lastLocation;
    }

    public PlayerData(UUID uuid, Location lastLocation) {
        this.uuid = uuid;
        this.homes = new HashMap<>();
        this.defaultHome = null;
        this.lastLocation = lastLocation;
    }

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.homes = new HashMap<>();
        this.defaultHome = null;
        this.lastLocation = null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Map<String, Home> getHomes() {
        return homes;
    }

    public void setHomes(HashMap<String, Home> homes) {
        this.homes = homes;
    }

    public Home getDefaultHome() {
        return defaultHome;
    }

    public String getFormatedDefaultHome(){
        if(defaultHome == null) return "";
        Location location = defaultHome.getLocation();
        if(location.getWorld() == null) return "";
        return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

    public void setDefaultHome(Home defaultHome) {
        this.defaultHome = defaultHome;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }
}

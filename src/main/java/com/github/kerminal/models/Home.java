package com.github.kerminal.models;

import org.bukkit.Location;

public class Home {

    private String name;
    private Location location;
    private boolean isDefault;

    public Home(Location location) {
        this.name = "";
        this.location = location;
        this.isDefault = false;
    }

    public Home(String name, Location location, boolean isDefault) {
        this.name = name;
        this.location = location;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}

package com.github.kerminal.models;

import org.bukkit.Location;

public class Home {

    private String name;
    private transient Location location;
    private String locationFormated;
    private boolean isPublic;

    public Home(Location location) {
        this.name = "";
        this.location = location;
        this.isPublic = false;
        this.locationFormated = location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

    public Home(String name, Location location, boolean isPublic) {
        this.name = name;
        this.location = location;
        this.isPublic = isPublic;
        this.locationFormated = location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getFormatedLocation(){
        return locationFormated;
    }
}

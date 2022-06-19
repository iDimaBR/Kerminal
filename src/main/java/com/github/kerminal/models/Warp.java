package com.github.kerminal.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@AllArgsConstructor
@Getter @Setter
public class Warp {

    private String name;
    private Location location;
}

package com.github.kerminal.customevents;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerBackEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter @Setter
    private Player player;
    @Getter @Setter
    private Location backLocation;
    private boolean cancelled;

    public PlayerBackEvent(Player player, Location backLocation) {
        this.player = player;
        this.backLocation = backLocation;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
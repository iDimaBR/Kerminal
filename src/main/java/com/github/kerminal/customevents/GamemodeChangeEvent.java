package com.github.kerminal.customevents;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GamemodeChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Getter @Setter
    private Player player;
    @Getter @Setter
    private GameMode gamemode;

    private boolean cancelled;

    public GamemodeChangeEvent(Player player, GameMode gamemode) {
        this.player = player;
        this.gamemode = gamemode;
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
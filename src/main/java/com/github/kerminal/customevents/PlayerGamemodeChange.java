package com.github.kerminal.customevents;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerGamemodeChange extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter @Setter
    private Player player;
    @Getter @Setter
    private GameMode gamemode;

    public PlayerGamemodeChange(Player player, GameMode gamemode) {
        this.player = player;
        this.gamemode = gamemode;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
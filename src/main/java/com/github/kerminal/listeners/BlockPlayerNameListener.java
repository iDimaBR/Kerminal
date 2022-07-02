package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;


@AllArgsConstructor
public class BlockPlayerNameListener implements Listener {

    private Kerminal plugin;


    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        final LangController blockedNames = plugin.getLangController();

        final String playerName = e.getPlayer().getName();
        blockedNames.getStringList("BlockedPlayerNames").forEach(name -> {
            if(name.contains(playerName)) {
                e.setKickMessage("Troque de nick e tente novamente!");
                e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Troque de nick e tente novamente!");
            }
        });
    }
}

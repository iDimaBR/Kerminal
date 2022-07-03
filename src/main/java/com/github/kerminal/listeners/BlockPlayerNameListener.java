package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.stream.Collectors;

@AllArgsConstructor
public class BlockPlayerNameListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        final LangController messages = plugin.getLangController();
        final ConfigUtil config = plugin.getConfig();

        final String playerName = e.getPlayer().getName();
        config.getStringList("BlockedPlayerNames").forEach(name -> {
            if(name.contains(playerName)) {
                e.setKickMessage(
                        StringUtils.join(
                                messages.getStringList("Kick.BlockedName")
                                        .stream()
                                        .map($ -> $.replace("&","§"))
                                        .collect(Collectors.toList())
                                , "\n")
                );
                e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            }
        });
    }
}

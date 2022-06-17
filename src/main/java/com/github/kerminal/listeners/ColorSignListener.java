package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@AllArgsConstructor
public class ColorSignListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onsign(SignChangeEvent e) {
        final Player player = e.getPlayer();
        if (player.hasPermission("kerminal.sign.color")) {
            for (int i = 0; i < e.getLines().length; i++) {
                final String line = e.getLine(i).replace("&","§");
                e.setLine(i, line);
            }
        }
    }
}

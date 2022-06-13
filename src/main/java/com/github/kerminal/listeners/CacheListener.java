package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@AllArgsConstructor
public class CacheListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if(!plugin.getSQL().loadPlayer(uuid))
            player.kickPlayer("§cNão foi possível carregar suas informações, contacte um administrador.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        plugin.getSQL().savePlayer(uuid);
    }
}

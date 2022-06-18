package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.models.PlayerData;
import com.github.kerminal.storage.MySQL;
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
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();
        final PlayerData dataPlayer = plugin.getController().getDataPlayer(uuid);
        final MySQL storage = plugin.getStorage();
        storage.loadHomes(uuid);
        storage.loadKitsDelay(uuid);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        //plugin.getSQL().savePlayer(uuid);
    }
}

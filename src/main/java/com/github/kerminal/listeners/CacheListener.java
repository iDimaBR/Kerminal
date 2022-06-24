package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.models.PlayerData;
import com.github.kerminal.storage.dao.StorageRepository;
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
        final UUID uuid = e.getPlayer().getUniqueId();
        final StorageRepository storage = plugin.getRepository();
        storage.loadData(uuid);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        final UUID uuid = e.getPlayer().getUniqueId();
        plugin.getController().getDATALIST().remove(uuid);
    }
}

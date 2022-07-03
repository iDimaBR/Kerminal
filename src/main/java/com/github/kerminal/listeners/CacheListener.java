package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.models.PlayerData;
import com.github.kerminal.storage.dao.StorageRepository;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Utils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CacheListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();
        final StorageRepository storage = plugin.getRepository();
        final ConfigUtil config = plugin.getConfig();

        storage.loadData(uuid);
        Utils.setHeaderFooter(player,
                StringUtils.join(
                        config.getStringList("TAB.Header")
                                .stream()
                                .map($ -> $.replace("&","§"))
                                .collect(Collectors.toList())
                        , "\n"),
                StringUtils.join(
                        config.getStringList("TAB.Footer")
                                .stream()
                                .map($ -> $.replace("&","§"))
                                .collect(Collectors.toList())
                        , "\n"));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        final UUID uuid = e.getPlayer().getUniqueId();
        plugin.getController().getDATALIST().remove(uuid);
    }
}

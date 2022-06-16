package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;
import java.util.stream.Collectors;


public class PlayerActionsChangeListener implements Listener {

    private ConfigUtil config;

    public PlayerActionsChangeListener(Kerminal plugin) {
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        final Player victim = e.getEntity().getPlayer();
        final Player killer = e.getEntity().getKiller();

        String message = config.getString("Features.CustomMessages.PlayerDeath.DefaultMessage").replace("%victim%", victim.getName());
        switch(config.getString("Features.CustomMessages.PlayerDeath.Mode")){
            default:
                if(killer != null)
                    message = message.replace("%killer%", killer.getName());

                e.setDeathMessage(message);
                break;
            case "PRIVATE":
                e.setDeathMessage(null);
                if(killer == null) break;

                String killerMsg = config.getString("Features.CustomMessages.PlayerDeath.PrivateAnnounces.Killer");
                killer.sendMessage(killerMsg.replace("%player%", victim.getName()));

                String victimMsg = config.getString("Features.CustomMessages.PlayerDeath.PrivateAnnounces.Victim");
                victim.sendMessage(victimMsg.replace("%player%", killer.getName()));
                break;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        final String message = config.getString("Features.CustomMessages.PlayerJoin").replace("%player%", e.getPlayer().getName());
        e.setJoinMessage(message);
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        final String message = config.getString("Features.CustomMessages.PlayerQuit").replace("%player%", e.getPlayer().getName());
        e.setLeaveMessage(message);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        final String message = config.getString("Features.CustomMessages.PlayerQuit").replace("%player%", e.getPlayer().getName());
        e.setQuitMessage(message);
    }
}

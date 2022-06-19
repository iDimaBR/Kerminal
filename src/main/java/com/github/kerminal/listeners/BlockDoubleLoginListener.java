package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.List;

@AllArgsConstructor
public class BlockDoubleLoginListener implements Listener {

    private Kerminal plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void aoTentarEntrar(PlayerLoginEvent e) {
        Player playerEvent = e.getPlayer();
        Player player = Bukkit.getPlayerExact(playerEvent.getName());
        if(player == null) return;

        e.setKickMessage("§cO jogador §f" + player.getName() + " §cjá está conectado.");
        e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void aoSerKickado(PlayerKickEvent e) {
        if (e.getReason().contains("logged in from another location"))
            e.setCancelled(true);
    }

}

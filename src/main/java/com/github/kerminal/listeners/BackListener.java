package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.DataController;
import com.github.kerminal.models.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BackListener implements Listener {

    private final Kerminal plugin;

    public BackListener(Kerminal plugin) {
        this.plugin = plugin;
    }

    public void onDeath(PlayerDeathEvent e){
        final DataController controller = plugin.getController();
        final Player player = e.getEntity().getPlayer();
        if(player == null) return;
        if(!player.hasPermission("kerminal.back")) return;
        if((player.getUniqueId()) == null) return;

        final PlayerData dataPlayer = controller.getDataPlayer(player.getUniqueId());
        dataPlayer.setLastLocation(player.getLocation());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        final DataController controller = plugin.getController();
        final Player player = e.getPlayer();
        if(!isValid(e.getCause())) return;
        if(!player.hasPermission("kerminal.back")) return;

        final PlayerData dataPlayer = controller.getDataPlayer(player.getUniqueId());
        dataPlayer.setLastLocation(e.getFrom());
    }

    private boolean isValid(PlayerTeleportEvent.TeleportCause cause) {
        return cause == PlayerTeleportEvent.TeleportCause.COMMAND || cause == PlayerTeleportEvent.TeleportCause.PLUGIN;
    }

}

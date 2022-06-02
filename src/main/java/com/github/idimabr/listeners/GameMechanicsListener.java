package com.github.idimabr.listeners;

import com.github.idimabr.Kerminal;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;


@AllArgsConstructor
public class GameMechanicsListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        final Player player = e.getEntity().getPlayer();
        e.setDeathMessage(null);

        if (!player.hasPermission("kerminal.keepxp")) return;
        e.setKeepLevel(true);
        e.setDroppedExp(0);
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        e.setLeaveMessage("");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }

    @EventHandler
    public void onMotd(ServerListPingEvent e){
        e.setMaxPlayers(plugin.getConfig().getInt("Motd.Maxplayers"));
        e.setMotd(plugin.getConfig().getString("Motd.NormalMotd").replace("&","§"));
    }
}

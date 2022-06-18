package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.List;

@AllArgsConstructor
public class BlockPlayerFlyListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onFly(PlayerToggleFlightEvent e) {
        final Player player = e.getPlayer();
        if(player.hasPermission("kerminal.blockfly.bypass")) return;

        final ConfigUtil config = plugin.getConfig();
        final List<String> blockFlyList = config.getStringList("BlockWorldsFly");
        if(blockFlyList.contains(player.getWorld().getName())){
            e.setCancelled(true);
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    @EventHandler
    public void onTeleportFly(PlayerChangedWorldEvent e) {
        final Player player = e.getPlayer();
        if(player.hasPermission("kerminal.blockfly.bypass")) return;

        final ConfigUtil config = plugin.getConfig();
        final List<String> blockFlyList = config.getStringList("BlockWorldsFly");
        if(blockFlyList.contains(player.getWorld().getName())){
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

}

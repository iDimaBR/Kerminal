package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

@AllArgsConstructor
public class BlockCommandListener implements Listener {

    private ConfigUtil config;

    public BlockCommandListener(Kerminal plugin) {
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onBlockCommand(PlayerCommandPreprocessEvent e) {
        final String executed = e.getMessage();
        final Player player = e.getPlayer();
        if(player.hasPermission("kerminal.blockedcommands.bypass")) return;

        for (String blockCMD : config.getStringList("BlockedCommands")) {
            if(executed.startsWith("/" + blockCMD)){
                player.sendMessage("§cComando bloqueado!");
                e.setCancelled(true);
                return;
            }
        }
    }
}

package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Utils;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

@AllArgsConstructor
public class TitleJoinListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        final Player player = e.getPlayer();
        final ConfigUtil config = plugin.getConfig();

        if(config.getBoolean("Welcome.SendTitle.Enabled")) {
            final String title = config.getString("Welcome.SendTitle.Title");
            final String subtitle = config.getString("Welcome.SendTitle.Subtitle");
            Utils.sendTitle(player, title, subtitle);
        }

        if(config.getBoolean("Welcome.Message.Enabled")) {
            final List<String> message = config.getStringList("Welcome.Message.Text");
            message.forEach(player::sendMessage);
        }
    }
}

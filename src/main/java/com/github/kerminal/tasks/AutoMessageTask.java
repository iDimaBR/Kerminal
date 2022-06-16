package com.github.kerminal.tasks;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class AutoMessageTask extends BukkitRunnable {

    private Kerminal plugin;

    private ConfigUtil config;
    private List<String> messages;
    private Sound sound;
    private int actualMessage = 0;


    public AutoMessageTask(Kerminal plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.messages = config.getStringList("Features.AutoMessage.Messages");

        String soundName = config.getString("Features.AutoMessage.Sound");
        if(validSound(soundName))
            this.sound = Sound.valueOf(soundName);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(sound != null)
                player.playSound(player.getLocation(), sound, 1, 1);

            player.sendMessage(messages.get(actualMessage));
        }

        actualMessage = (actualMessage == (messages.size()-1) ? 0 : actualMessage + 1);
    }

    private boolean validSound(String name){
        return Arrays.stream(Sound.values()).anyMatch($ -> $.name().equalsIgnoreCase(name));
    }
}

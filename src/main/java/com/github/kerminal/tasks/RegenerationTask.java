package com.github.kerminal.tasks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RegenerationTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(player.isDead()) continue;
            if(player.getGameMode().equals(GameMode.CREATIVE)) continue;
            if(player.getGameMode().equals(GameMode.SPECTATOR)) continue;
            if((player.getHealth() + 0.5) >= player.getMaxHealth()) continue;

            player.setHealth(Math.max(0, player.getHealth() + 0.5));
        }
    }
}

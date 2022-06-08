package com.github.kerminal.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RegenerationTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(player.getHealth() == player.getMaxHealth()) continue;

            player.setHealth(Math.max(0, player.getHealth() + 0.5));
            Bukkit.getPluginManager().callEvent(new EntityRegainHealthEvent(player, 0.5, EntityRegainHealthEvent.RegainReason.MAGIC));
        }
    }
}

package com.github.idimabr.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RegenerationTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.getPluginManager().callEvent(new EntityRegainHealthEvent(player, 0, EntityRegainHealthEvent.RegainReason.CUSTOM));
        }
    }
}

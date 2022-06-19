package com.github.kerminal.tasks;

import com.github.kerminal.registry.TeleportRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;

public class TeleportTask implements Runnable{

    private final TeleportRegistry registry;

    public TeleportTask(TeleportRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void run() {
        final Map<UUID, Pair<Location, Long>> map = registry.getAll();
        for (Map.Entry<UUID, Pair<Location, Long>> entry : map.entrySet()) {

            final UUID uniqueId = entry.getKey();
            final Pair<Location, Long> pair = entry.getValue();
            final Location location = pair.getKey();
            final long expiry = pair.getValue();

            if (!isExpired(expiry)) continue;
            map.remove(uniqueId);

            Player player = Bukkit.getPlayer(uniqueId);
            if (player == null) continue;

            registry.teleport(player, location);
        }
    }

    private boolean isExpired(long time) {
        return time <= System.currentTimeMillis();
    }

}

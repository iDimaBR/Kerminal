package com.github.kerminal.registry;

import com.github.kerminal.utils.TimeUtils;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class TeleportRegistry {

    private final int DEFAULT_DELAY = 3;

    private final Map<UUID, Tuple<Location, Long>> longMap;

    public TeleportRegistry() {
        this.longMap = new ConcurrentHashMap<>();
    }

    public TeleportRegistry register(Player player, Location location) {
        register(player.getUniqueId(), location);
        return this;
    }

    public void register(UUID uniqueId, Location location) {
        this.longMap.put(uniqueId, new Tuple<>(location, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(DEFAULT_DELAY))); //
    }

    public void remove(UUID uniqueId) {
        this.longMap.remove(uniqueId);
    }

    public Map<UUID, Tuple<Location, Long>> getAll() {
        return longMap;
    }

    public String getDelay(Player player) {
        return TimeUtils.formatOneLetter(longMap.get(player.getUniqueId()).b());
    }

    public int getDefaultDelay() {
        return DEFAULT_DELAY;
    }
}

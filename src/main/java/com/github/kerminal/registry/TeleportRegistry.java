package com.github.kerminal.registry;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TeleportRegistry {

    private final Kerminal plugin;
    private final int DEFAULT_DELAY;
    private Map<UUID, Tuple<Location, Long>> longMap;


    public TeleportRegistry(Kerminal plugin) {
        this.plugin = plugin;
        this.DEFAULT_DELAY = plugin.getConfig().getInt("Teleport.Delay");
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

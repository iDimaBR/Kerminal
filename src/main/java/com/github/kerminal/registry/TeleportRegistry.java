package com.github.kerminal.registry;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TeleportRegistry {

    private final Kerminal plugin;
    private final int DEFAULT_DELAY;
    private final int DEFAULT_INVENCIBLE_DELAY;
    private Map<UUID, Pair<Location, Long>> longMap;


    public TeleportRegistry(Kerminal plugin) {
        this.plugin = plugin;
        this.DEFAULT_DELAY = plugin.getConfig().getInt("Teleport.Delay");
        this.DEFAULT_INVENCIBLE_DELAY = plugin.getConfig().getInt("Teleport.InvencibleDelay");
        this.longMap = new ConcurrentHashMap<>();
    }
    public TeleportRegistry register(Player player, Location location, boolean silent) {
        register(player.getUniqueId(), location, silent);
        return this;
    }

    public void register(UUID uniqueId, Location location, boolean silent) {
        if(!silent) Bukkit.getPlayer(uniqueId).sendMessage("§aTeleportando em " + DEFAULT_DELAY + " segundos.");
        this.longMap.put(uniqueId, Pair.of(location, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(DEFAULT_DELAY))); //
    }

    public void remove(UUID uniqueId) {
        this.longMap.remove(uniqueId);
    }

    public Map<UUID, Pair<Location, Long>> getAll() {
        return longMap;
    }

    public String getDelay(Player player) {
        return TimeUtils.formatOneLetter(longMap.get(player.getUniqueId()).getValue());
    }

    public int getDefaultDelay() {
        return DEFAULT_DELAY;
    }

    public void teleport(Player player, Location location) {
        player.teleport(location);
        player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null, 3);
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
        player.sendMessage("§aTeleportado!");
        player.setNoDamageTicks(DEFAULT_INVENCIBLE_DELAY * 20);
    }
}

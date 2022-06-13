package com.github.kerminal.tasks;

import com.github.kerminal.Kerminal;
import com.github.kerminal.registry.TeleportRegistry;
import net.minecraft.server.v1_8_R3.Tuple;
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
        final Map<UUID, Tuple<Location, Long>> map = registry.getAll();
        for (Map.Entry<UUID, Tuple<Location, Long>> entry : map.entrySet()) {

            final UUID uniqueId = entry.getKey();
            final Tuple<Location, Long> tuple = entry.getValue();
            final Location location = tuple.a();
            final long expiry = tuple.b();

            if (!isExpired(expiry)) continue;
            map.remove(uniqueId);

            Player player = Bukkit.getPlayer(uniqueId);
            if (player == null) continue;

            location.setYaw(player.getLocation().getYaw());
            location.setPitch(player.getLocation().getPitch());
            player.teleport(location);
            player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null, 3);
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
            player.sendMessage("§aTeleportado!");
        }
    }

    private boolean isExpired(long time) {
        return time <= System.currentTimeMillis();
    }

}

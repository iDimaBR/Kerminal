package com.github.idimabr.listeners;

import com.github.idimabr.Kerminal;
import com.github.idimabr.utils.ConfigUtil;
import com.github.idimabr.utils.TimeUtils;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.util.NumberConversions;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RequiredArgsConstructor
public class ConfigurableCommandsHandler implements Listener {

    private final Kerminal plugin;
    private HashMap<UUID, Map<String, Long>> delayCommand = Maps.newHashMap();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        final ConfigUtil commands = plugin.getConfigurableCommands();
        final String msg = e.getMessage().substring(1).toLowerCase();
        final Player player = e.getPlayer();

        final String[] args = msg.contains(" ") ? msg.split(" ") : new String[]{msg};
        final ConfigurationSection commandSettings = commands.getConfigurationSection(args[0]);
        if (commandSettings == null) return;
        e.setCancelled(true);

        if(commandSettings.isSet("Delay")) {
            if (!applyDelay(player, args[0], commandSettings)) {
                final Long delay = delayCommand.getOrDefault(player.getUniqueId(), new HashMap<>()).getOrDefault(args[0], 0L);
                player.sendMessage(
                        commandSettings.isSet("MessageDelay") ?
                                commandSettings.getString("MessageDelay")
                                        .replace("&","§")
                                        .replace("%time%", formatDelay(delay)) :
                                "§cAguarde " + formatDelay(delay) + " para usar novamente."
                );
                return;
            }
        }

        sendMessage(player, commandSettings);
        playSound(player, commandSettings);
        teleportPlayer(player, commandSettings);
    }

    private boolean applyDelay(Player player, String cmd, ConfigurationSection commandSettings){
        final Map<String, Long> delaysOfPlayer = delayCommand.getOrDefault(player.getUniqueId(), new HashMap<>());
        final Long delay = delaysOfPlayer.getOrDefault(cmd, 0L);
        if(delay > System.currentTimeMillis()) return false;

        final int commandDelay = commandSettings.getInt("Delay");

        delaysOfPlayer.put(cmd, System.currentTimeMillis() + (1000L * commandDelay));
        delayCommand.put(player.getUniqueId(), delaysOfPlayer);
        return true;
    }

    private void sendMessage(Player player, ConfigurationSection commandSettings){
        if (!commandSettings.isSet("Message")) return;
        commandSettings.getStringList("Message")
                .stream()
                .map($ -> $.replace("&", "§").replace("%player%", player.getName()))
                .forEach(player::sendMessage);
    }

    private void teleportPlayer(Player player, ConfigurationSection commandSettings){
        if(!commandSettings.isSet("Location")) return;
        Location location = getLocation(commandSettings.getConfigurationSection("Location").getValues(true));
        if(location == null) return;
        player.teleport(location);
    }

    private void playSound(Player player, ConfigurationSection commandSettings){
        if(!commandSettings.isSet("Sound")) return;
        String sound = commandSettings.getString("Sound");
        if(isInvalidSound(sound)) return;
        player.playSound(player.getLocation(), Sound.valueOf(sound), 1, 1);
    }

    private Location getLocation(Map<String, Object> args) {
        World world = Bukkit.getWorld((String)args.get("world"));
        if(world == null) return null;
        if(!args.containsKey("x")) return null;
        if(!args.containsKey("y")) return null;
        if(!args.containsKey("z")) return null;

        return new Location(
                world,
                NumberConversions.toDouble(args.get("x")),
                NumberConversions.toDouble(args.get("y")),
                NumberConversions.toDouble(args.get("z"))
        );
    }

    private boolean isInvalidSound(String sound){
        return Arrays.stream(Sound.values()).noneMatch($ -> $.name().equalsIgnoreCase(sound));
    }

    public static String formatDelay(Long delay) {
        return TimeUtils.formatOneLetter(delay - System.currentTimeMillis());
    }
}

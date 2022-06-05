package com.github.idimabr.listeners;

import com.github.idimabr.Kerminal;
import com.github.idimabr.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.util.NumberConversions;

import java.util.Arrays;
import java.util.Map;


@AllArgsConstructor
public class ConfigurableCommandsHandler implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        final ConfigUtil commands = plugin.getConfigurableCommands();
        final String message = e.getMessage().substring(1);
        final Player player = e.getPlayer();

        final String[] args = message.contains(" ") ? message.split(" ") : new String[]{message};
        final ConfigurationSection commandSettings = commands.getConfigurationSection(args[0].toLowerCase());
        if (commandSettings == null) return;
        e.setCancelled(true);

        sendMessage(player, commandSettings);
        playSound(player, commandSettings);
        teleportPlayer(player, commandSettings);
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
}

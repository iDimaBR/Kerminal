package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.DataController;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.customevents.PlayerHomeTeleportEvent;
import com.github.kerminal.models.Home;
import com.github.kerminal.models.PlayerData;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class HomeCommand {

    private Kerminal plugin;
    private ConfigUtil commands;
    private final String identifierCommand = "Home";
    private String command;
    private String[] aliases;
    private String permission;

    public HomeCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, @Optional String nameHome) {
        final CommandSender sender = context.getSender();
        final LangController messages = plugin.getLangController();
        final Player player = (Player) sender;
        final int args = context.argsCount();
        final DataController controller = plugin.getController();
        final TeleportRegistry teleportRegistry = controller.getRegistryTeleport();

        final PlayerData data = controller.getDataPlayer(player.getUniqueId());
        if(args == 0) {
            final Home defaultHome = data.getDefaultHome();
            if (defaultHome == null) {
                player.sendMessage(messages.getString("Commands.HomeSection.DefaultHomeWarning").replace("%command%", commands.getString("Sethome.command") + " " + messages.getString("Commands.HomeSection.NameOfDefaultHome")));
                return;
            }

            PlayerHomeTeleportEvent playerHomeTeleportEvent = new PlayerHomeTeleportEvent(player, defaultHome);
            Bukkit.getPluginManager().callEvent(playerHomeTeleportEvent);
            if(playerHomeTeleportEvent.isCancelled()) return;

            final Location location = defaultHome.getLocation();

            if (player.hasPermission(permission + ".delay.bypass")) {
                teleportRegistry.teleport(player, location);
                return;
            }

            teleportRegistry.register(player, location, true);
            player.sendMessage(messages.getString("Commands.HomeSection.Home.Teleporting").replace("%home%", "casa principal").replace("%delay%", teleportRegistry.getDefaultDelay()+""));
        }else{
            if(data.getHomes().containsKey(nameHome)) {
                final Home home = data.getHomes().get(nameHome);
                final Location location = home.getLocation();

                PlayerHomeTeleportEvent playerHomeTeleportEvent = new PlayerHomeTeleportEvent(player, home);
                Bukkit.getPluginManager().callEvent(playerHomeTeleportEvent);
                if(playerHomeTeleportEvent.isCancelled()) return;

                if(player.hasPermission(permission + ".delay.bypass")){
                    teleportRegistry.teleport(player, location);
                    return;
                }

                teleportRegistry.register(player, location, true);
                player.sendMessage(messages.getString("Commands.HomeSection.Home.Teleporting").replace("%home%", home.getName()).replace("%delay%", teleportRegistry.getDefaultDelay()+""));
            }else{
                player.sendMessage(messages.getString("Commands.HomeSection.Home.NotFound").replace("%home%", nameHome));
            }
        }
    }

    public void register(){
        if (!commands.getBoolean(identifierCommand + ".enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(command)
                        .aliases(aliases)
                        .permission(permission)
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }
}

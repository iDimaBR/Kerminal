package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.DataController;
import com.github.kerminal.customevents.PlayerHomeTeleportEvent;
import com.github.kerminal.models.Home;
import com.github.kerminal.models.PlayerData;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class HomeCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public HomeCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Home.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Home.command"))
                        .aliases(commands.getStringList("Home.aliases").toArray(new String[0]))
                        .permission(commands.getString("Home.permission"))
                        .async(commands.getBoolean("Home.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context, @Optional String nameHome) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) sender;
        final int args = context.argsCount();
        final DataController controller = plugin.getController();
        final TeleportRegistry teleportRegistry = controller.getRegistryTeleport();

        final PlayerData data = controller.getDataPlayer(player.getUniqueId());
        if(data == null){
            player.sendMessage("§cSuas informações não foram carregadas, entre novamente no servidor.");
            return;
        }

        if(isInvalidWorld(player.getLocation())){
            player.sendMessage("§cNesse mundo não é permitido definir homes.");
            return;
        }

        if(args == 0) {
            final Home defaultHome = data.getDefaultHome();
            if (defaultHome == null) {
                player.sendMessage("§cSua casa principal não foi definida ainda.");
                return;
            }

            PlayerHomeTeleportEvent playerHomeTeleportEvent = new PlayerHomeTeleportEvent(player, defaultHome);
            Bukkit.getPluginManager().callEvent(playerHomeTeleportEvent);
            if(playerHomeTeleportEvent.isCancelled()) return;

            final Location location = defaultHome.getLocation();

            if (player.hasPermission(commands.getString("Home.permission") + ".delay.bypass")) {
                teleportRegistry.teleport(player, location);
                return;
            }

            teleportRegistry.register(player, location);
            player.sendMessage("§aTeleportando para casa principal em " + teleportRegistry.getDelay(player) + " segundos.");
        }else{
            if(data.getHomes().containsKey(nameHome)) {
                final Home home = data.getHomes().get(nameHome);
                final Location location = home.getLocation();

                PlayerHomeTeleportEvent playerHomeTeleportEvent = new PlayerHomeTeleportEvent(player, home);
                Bukkit.getPluginManager().callEvent(playerHomeTeleportEvent);
                if(playerHomeTeleportEvent.isCancelled()) return;

                if(player.hasPermission(commands.getString("Home.permission") + ".delay.bypass")){
                    teleportRegistry.teleport(player, location);
                    return;
                }

                teleportRegistry.register(player, location);
                player.sendMessage("§aTeleportando para casa '§f" + home.getName() + "§a' em " + teleportRegistry.getDelay(player) + " segundos.");
            }else{
                player.sendMessage("§cCasa não foi encontrada!");
            }
        }
    }

    private boolean isInvalidWorld(Location location) {
        return plugin.getConfig().getStringList("HomeSystem.BlacklistWorlds").contains(location.getWorld().getName());
    }
}

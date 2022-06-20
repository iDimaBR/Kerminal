package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.models.Warp;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public class WarpCommand {

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Warp";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public WarpCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, String warpName) {
        final LangController messages = plugin.getLangController();
        final Player player = (Player) context.getSender();
        final TeleportRegistry teleportRegistry = plugin.getTeleportRegistry();
        warpName = warpName.toLowerCase();

        final Map<String, Warp> warpsList = plugin.getWarpsList();
        final Warp warp = warpsList.get(warpName);
        if(warp == null){
            player.sendMessage(messages.getString("Commands.WarpSection.Warp.NoExist").replace("%warp%", warpName));
            return;
        }

        if (player.hasPermission(permission + "." + warpName)) {
            player.sendMessage(messages.getString("DefaultCallback.NoPermission"));
            return;
        }

        if (player.hasPermission(permission + ".delay.bypass")) {
            teleportRegistry.teleport(player, warp.getLocation());
            return;
        }

        teleportRegistry.register(player, warp.getLocation(), false);
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

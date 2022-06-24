package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.models.Warp;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.LocationUtils;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public class SetWarpCommand {

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "SetWarp";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public SetWarpCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, String warpName) {
        final LangController messages = plugin.getLangController();
        final Player player = (Player) context.getSender();

        if(warpName == null){
            player.sendMessage(messages.getString("Commands.WarpSection.Setwarp.Usage").replace("%command%", command));
            return;
        }

        warpName = warpName.toLowerCase();
        final Map<String, Warp> warpsList = plugin.getWarpsList();
        if(warpsList.get(warpName) != null){
            player.sendMessage(messages.getString("Commands.WarpSection.Setwarp.ExistWarp").replace("%warp%", warpName));
            return;
        }

        final ConfigUtil locations = plugin.getLocations();
        final Warp warp = new Warp(warpName, player.getLocation());
        LocationUtils.setLocationConfig(locations, player.getLocation(), "Warps." + warpName);
        warpsList.put(warpName, warp);
        player.sendMessage(messages.getString("Commands.WarpSection.Setwarp.Success").replace("%warp%", warpName));
    }

    public void register(){
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

package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
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

    private Kerminal plugin;
    private ConfigUtil commands;

    public SetWarpCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("SetWarp.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("SetWarp.command"))
                        .aliases(commands.getStringList("SetWarp.aliases").toArray(new String[0]))
                        .permission(commands.getString("SetWarp.permission"))
                        .async(commands.getBoolean("SetWarp.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context, String warpName) {
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) context.getSender();
        warpName = warpName.toLowerCase();

        final Map<String, Warp> warpsList = plugin.getWarpsList();
        if(warpsList.get(warpName) != null){
            player.sendMessage("§cA warp'" + warpName + "' já existe!");
            return;
        }

        final ConfigUtil locations = plugin.getLocations();
        final Warp warp = new Warp(warpName, player.getLocation());
        LocationUtils.setLocationConfig(locations, player.getLocation(), "Warps." + warpName);
        warpsList.put(warpName, warp);
        player.sendMessage("§aVocê definiu a warp '" + warpName + "'");
    }
}

package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
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

    private Kerminal plugin;
    private ConfigUtil commands;

    public WarpCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Warp.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Warp.command"))
                        .aliases(commands.getStringList("Warp.aliases").toArray(new String[0]))
                        .permission(commands.getString("Warp.permission"))
                        .async(commands.getBoolean("Warp.async"))
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
        final TeleportRegistry teleportRegistry = plugin.getTeleportRegistry();
        warpName = warpName.toLowerCase();

        final Map<String, Warp> warpsList = plugin.getWarpsList();
        final Warp warp = warpsList.get(warpName);
        if(warp == null){
            player.sendMessage("§cA warp'" + warpName + "' não existe!");
            return;
        }

        if (player.hasPermission(commands.getString("Warp.permission") + ".delay.bypass")) {
            teleportRegistry.teleport(player, warp.getLocation());
            return;
        }

        teleportRegistry.register(player, warp.getLocation());
    }
}

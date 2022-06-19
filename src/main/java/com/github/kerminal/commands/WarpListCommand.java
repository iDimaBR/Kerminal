package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.models.Warp;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public class WarpListCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public WarpListCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("WarpList.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("WarpList.command"))
                        .aliases(commands.getStringList("WarpList.aliases").toArray(new String[0]))
                        .permission(commands.getString("WarpList.permission"))
                        .async(commands.getBoolean("WarpList.async"))
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) context.getSender();
        final Map<String, Warp> warpsList = plugin.getWarpsList();

        player.sendMessage("§aWarps: §f" + (warpsList.size() == 0 ? "Nenhum" : StringUtils.join(warpsList.keySet(), "§7,§f ")));
    }
}

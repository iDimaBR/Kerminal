package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.stream.Collectors;

@AllArgsConstructor
public class OnlinePlayersCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public OnlinePlayersCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Onlines.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Jogadores.command"))
                        .aliases(commands.getStringList("Jogadores.aliases").toArray(new String[0]))
                        .permission(commands.getString("Jogadores.permission"))
                        .async(commands.getBoolean("Jogadores.async"))
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        context.sendMessage("§aJogadores: §f" +
                StringUtils.join(
                        Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()),
                        ", "
                )
        );
    }

}

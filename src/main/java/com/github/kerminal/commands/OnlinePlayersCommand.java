package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
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

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "OnlinePlayers";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public OnlinePlayersCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context) {
        final LangController messages = plugin.getLangController();
        context.sendMessage(
                messages.getString("Commands.OnlinePlayers.Listing")
                        .replace("%playerlist%",
                                StringUtils.join(
                                        Bukkit.getOnlinePlayers()
                                                .stream()
                                                .map(HumanEntity::getName)
                                                .collect(Collectors.toList()), ", ")
                        ));
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
                    onCommand(context);
                    return false;
                }
        );
    }

}

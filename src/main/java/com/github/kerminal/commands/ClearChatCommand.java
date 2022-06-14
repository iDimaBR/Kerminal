package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

@AllArgsConstructor
public class ClearChatCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public ClearChatCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("ClearChat.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("ClearChat.command"))
                        .aliases(commands.getStringList("ClearChat.aliases").toArray(new String[0]))
                        .permission(commands.getString("ClearChat.permission"))
                        .async(commands.getBoolean("ClearChat.async"))
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();

        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        onlinePlayers.forEach($ -> $.sendMessage(StringUtils.repeat(" \n", 100)));
        onlinePlayers.forEach($ -> $.sendMessage("§aO chat foi limpo!"));
    }

}

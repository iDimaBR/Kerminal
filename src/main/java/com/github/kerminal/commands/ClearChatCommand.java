package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

@AllArgsConstructor
public class ClearChatCommand {

    private Kerminal plugin;

    @Command(
            name = "clearchat",
            aliases = {"cchat","cc"},
            permission = "kerminal.clearchat"
    )
    public void onClear(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();

        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        onlinePlayers.forEach($ -> $.sendMessage(StringUtils.repeat(" \n", 100)));
        onlinePlayers.forEach($ -> $.sendMessage("§aO chat foi limpo!"));
    }

}

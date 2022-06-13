package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
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

    @Command(
            name = "jogadores",
            permission = "kerminal.jogadores"
    )
    public void onPlayersOnline(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        context.sendMessage("§aJogadores: §f" +
                StringUtils.join(
                        Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()),
                        ", "
                )
        );
    }

}

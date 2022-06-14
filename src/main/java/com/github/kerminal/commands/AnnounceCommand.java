package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.TimeUtils;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AnnounceCommand {

    private final Kerminal plugin;
    private final HashMap<UUID, Long> DELAY = Maps.newHashMap();

    @Command(
            name = "divulgar",
            aliases = {"anunciar"},
            permission = "kerminal.divulgar",
            target = CommandTarget.PLAYER
    )
    public void onAnnounce(Context<CommandSender> context) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) sender;

        if(DELAY.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis()){
            player.sendMessage("§cVocê tem que aguardar " + TimeUtils.formatOneLetter(DELAY.get(player.getUniqueId()) - System.currentTimeMillis()));
            return;
        }

        StringBuilder builder = new StringBuilder();
        for(int i = 0;i < context.argsCount();i++)
            builder.append(context.getArg(i)).append(" ");

        final String announce = builder.toString();
        DELAY.put(player.getUniqueId(), System.currentTimeMillis() + 3000);

        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        List<String> formated = messages.getStringList("Commands.DivulgarFormat").stream().map(
                $ -> $.replace("&", "§")
                        .replace("%player%", player.getDisplayName())
                        .replace("%message%", announce)
        ).collect(Collectors.toList());

        for (Player onlinePlayer : onlinePlayers) {
            formated.forEach(onlinePlayer::sendMessage);
        }
    }

}
package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class OnlineCommand {

    private Kerminal plugin;

    @Command(
            name = "onlines",
            aliases = {"online"},
            permission = "kerminal.onlines"
    )
    public void onPlayersOnline(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();

        context.sendMessage("§aJogadores: §f" + Bukkit.getOnlinePlayers().size() + " onlines");
    }

}

package com.github.idimabr.commands;


import com.github.idimabr.Kerminal;
import com.github.idimabr.customevents.PlayerGamemodeChange;
import com.github.idimabr.utils.ConfigUtil;
import com.github.idimabr.utils.Mode;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.saiintbrisson.minecraft.command.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class GamemodeCommand {

    private Kerminal plugin;
    @Command(
            name = "gamemode",
            aliases = {"gm"},
            description = "Muda seu gamemode :P",
            permission = "kerminal.gamemode"
    )
    public void onGamemode(Context<CommandSender> context, @Optional String gamemode, @Optional String target) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();

        if(gamemode == null){
            sender.sendMessage(messages.getString("Gamemode.Usage"));
            return;
        }

        final Mode mode = Mode.of(gamemode);
        if(mode == null){
            sender.sendMessage(messages.getString("Gamemode.Usage"));
            return;
        }

        final String nameOfMode = StringUtils.capitalize(mode.name().toLowerCase());
        if(target == null){
            final Player player = (Player) sender;
            player.setGameMode(mode.getGameMode());
            player.sendMessage(
                    messages.getString("Gamemode.Change")
                            .replace("%player%", player.getName())
                            .replace("%mode%", nameOfMode)
            );
            Bukkit.getPluginManager().callEvent(new PlayerGamemodeChange(player, mode));
            return;
        }

        final Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer == null) {
            sender.sendMessage(messages.getString("Returns.PlayerNotFound"));
            return;
        }


        targetPlayer.setGameMode(mode.getGameMode());
        targetPlayer.sendMessage(
                messages.getString("Gamemode.TargetChange")
                        .replace("%player%", targetPlayer.getName())
                        .replace("%mode%", nameOfMode)
        );
        Bukkit.getPluginManager().callEvent(new PlayerGamemodeChange(targetPlayer, mode));
    }
}

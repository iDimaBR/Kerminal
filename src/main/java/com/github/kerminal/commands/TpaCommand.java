package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.storage.dao.StorageRepository;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.TimeUtils;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TpaCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public TpaCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if (!commands.getBoolean("Tpa.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Tpa.command"))
                        .aliases(commands.getStringList("Tpa.aliases").toArray(new String[0]))
                        .permission(commands.getString("Tpa.permission"))
                        .async(commands.getBoolean("Tpa.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> c, @Optional String target) {
        final LangController messages = plugin.getLangController();
        final CommandSender sender = c.getSender();
        final Player player = (Player) sender;
        final Player targetPlayer = Bukkit.getPlayer(target);

        if (target == null) {
            player.sendMessage(messages.getString("Tpa.Usage"));
            return;
        }
        if (targetPlayer == null) {
            player.sendMessage(messages.getString("Tpa.PlayerNotFound"));
            return;
        }
        if (target != null) {
            if (targetPlayer != null) {
                if (plugin.getRepository().isRequest(player.getUniqueId(), targetPlayer.getName())) {
                    player.sendMessage(messages.getString("Tpa.AlreadyRequested"));
                    return;
                }

                player.sendMessage(messages.getString("Tpa.RequestSent"));
                targetPlayer.sendMessage(messages.getString("Tpa.RequestReceived").replace("%player%", player.getName()));
                plugin.getRepository().insertRequest(player.getUniqueId(), targetPlayer.getName());
            }
        }
    }
}

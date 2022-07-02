package com.github.kerminal.commands;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class TitleToPlayerCommand {
    private Kerminal plugin;

    private ConfigUtil commands;


    private final String identifierCommand = "TitleToPlayer";

    private final String command;


    private final String[] aliases;

    private final String permission;

    public TitleToPlayerCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(
            Context<CommandSender> context,
            String target,
            String message,
            @Optional String message2
    ) {

        if (target != null) {
            if (message != null) {
                if (message2 == null) {
                    message2 = "";
                }
                final Player player = plugin.getServer().getPlayer(target);
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', message), ChatColor.translateAlternateColorCodes('&', message2));
            } else {
                context.sendMessage("§cVocê não pode enviar uma mensagem vazia!");
            }
        } else {
            context.sendMessage("§cVocê precisa de um jogador alvo!");
        }
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
                    onCommand(context, context.getArg(0), context.getArg(1), context.getArg(2));
                    return false;
                }
        );
    }

}



package com.github.kerminal.commands;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Utils;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SendTitleCommand {
    private Kerminal plugin;
    private ConfigUtil commands;
    private final String identifierCommand = "SendTitle";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public SendTitleCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context) {
        final CommandSender sender = context.getSender();
        final String[] args = context.getArgs();

        if(args.length < 3){
            sender.sendMessage("§cUtilize /" + command + " <jogador/all> <titulo/subtitulo> <texto>");
            return;
        }

        final String target = args[0];
        final String choose = args[1].toLowerCase();
        if(!target.equalsIgnoreCase("all")) {
            final Player targetPlayer = Bukkit.getPlayer(target);
            if (targetPlayer == null) {
                sender.sendMessage("§cJogador não encontrado!");
                return;
            }

            sendTitle(targetPlayer, args, choose);
            return;
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendTitle(onlinePlayer, args, choose);
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
                    onCommand(context);
                    return false;
                }
        );
    }

    private void sendTitle(Player target, String[] message, String choose){
        switch(choose){
            case "titulo":
            case "title":
                Utils.sendTitle(target, StringUtils.join(color(message), " ", 2, message.length), "");
                break;
            case "subtitulo":
            case "subtitle":
                Utils.sendTitle(target, "", StringUtils.join(color(message), " ", 2, message.length));
        }
    }

    private String[] color(String[] args){
        String[] newArgs = new String[args.length];
        int i = 0;
        for (String arg : args) {
            newArgs[i++] = arg.replace("&","§");
        }
        return newArgs;
    }
}



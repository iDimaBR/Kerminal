package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.TimeUtils;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class KerminalCommand {

    private Kerminal plugin;

    public KerminalCommand(Kerminal plugin) {
        this.plugin = plugin;
    }

    public void onCommand(Context<CommandSender> context) {
        final CommandSender sender = context.getSender();
        final int args = context.argsCount();

        if(args == 1 && context.getArg(0).equalsIgnoreCase("reload")){
            plugin.getConfig().saveWithComments();
            plugin.getMessages().saveWithComments();
            plugin.getLocations().save();
            plugin.getCommands().save();
            plugin.getConfigurableCommands().saveWithComments();
            plugin.getLangController().loadAllLangs();
            sender.sendMessage("§aConfiguração reiniciada!");
            return;
        }

        sender.sendMessage("§aComandos:");
        sender.sendMessage("");
        sender.sendMessage("§7    /kerminal reload - Reinicia as configurações");
        sender.sendMessage("");
    }

    public void register(){
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name("kerminal")
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

}

package com.github.kerminal.commands;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class BroadcastTitleCommand {
    private Kerminal plugin;

    private ConfigUtil commands;


    private final String identifierCommand = "BroadcastTitle";

    private final String command;


    private final String[] aliases;

    private final String permission;

    public BroadcastTitleCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }
    public void onCommand(Context<CommandSender> context, String message, @Optional String message2) {

        if (message != null) {
            if (message2 == null) {
                message2 = "";
            }
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', message), ChatColor.translateAlternateColorCodes('&', message2));
            }
        } else {
            context.sendMessage("§cVocê não pode enviar uma mensagem vazia!");
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
                    onCommand(context, context.getArg(0), context.getArg(1));
                    return false;
                }
        );
    }
}

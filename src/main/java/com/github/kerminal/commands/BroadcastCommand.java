package com.github.kerminal.commands;

import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class BroadcastCommand {
    private Kerminal plugin;
    private ConfigUtil commands;
    private final String identifierCommand = "Broadcast";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public BroadcastCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }
    public void onCommand(Context<CommandSender> context, String message) {
        if (message == null) {
            context.sendMessage("§cVocê não pode enviar uma mensagem vazia!");
            return;
        }

        plugin.getServer().broadcastMessage(message);
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
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }
}

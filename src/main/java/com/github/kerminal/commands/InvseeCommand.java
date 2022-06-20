package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Utils;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class InvseeCommand {

    private final Kerminal plugin;
    public static HashMap<String, String> inventories = new HashMap<>();
    private ConfigUtil commands;
    private final String identifierCommand = "Invsee";
    private String command;
    private String[] aliases;
    private String permission;

    public InvseeCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, @Optional String targetName) {
        final LangController messages = plugin.getLangController();
        final int args = context.argsCount();
        final Player player = (Player) context.getSender();

        if(args == 0){
            player.sendMessage(messages.getString("Commands.Invsee.Usage").replace("%command%", command));
            return;
        }

        final Player target = Utils.getPlayer(targetName);
        if(target == null){
            player.sendMessage(messages.getString("DefaultCallback.PlayerNotFound"));
            return;
        }

        player.openInventory(target.getInventory());
        inventories.put(player.getName(), target.getName());
        player.sendMessage(messages.getString("Commands.Invsee.Success").replace("%target%", target.getName()));
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

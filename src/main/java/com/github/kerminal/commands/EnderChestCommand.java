package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class EnderChestCommand {

    private final Kerminal plugin;
    public static HashMap<String, String> enderchests = new HashMap<>();
    private ConfigUtil commands;
    private final String identifierCommand = "Enderchest";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public EnderChestCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, @Optional String targetName) {
        final LangController messages = plugin.getLangController();
        final int args = context.argsCount();
        Player player = (Player) context.getSender();

        if(args == 0){
            player.openInventory(player.getEnderChest());
            return;
        }

        Player target = Utils.getPlayer(targetName);
        if(target == null){
            player.sendMessage(messages.getString("DefaultCallback.PlayerNotFound"));
            return;
        }

        player.openInventory(target.getEnderChest());
        enderchests.put(player.getName(), target.getName());
        player.sendMessage(messages.getString("Commands.Enderchest.Success"));
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

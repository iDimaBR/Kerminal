package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
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

    public EnderChestCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Enderchest.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Enderchest.command"))
                        .aliases(commands.getStringList("Enderchest.aliases").toArray(new String[0]))
                        .permission(commands.getString("Enderchest.permission"))
                        .async(commands.getBoolean("Enderchest.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context, @Optional String targetName) {
        final ConfigUtil messages = plugin.getMessages();
        final int args = context.argsCount();
        Player player = (Player) context.getSender();

        if(args == 0){
            player.openInventory(player.getEnderChest());
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if(target == null){
            player.sendMessage("§cJogador não encontrado!");
            return;
        }

        player.openInventory(target.getEnderChest());
        enderchests.put(player.getName(), target.getName());
        player.sendMessage("§aEnderchest de " + target.getName() + " foi aberto!");
    }

}

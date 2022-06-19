package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
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

    public InvseeCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Invsee.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Invsee.command"))
                        .aliases(commands.getStringList("Invsee.aliases").toArray(new String[0]))
                        .permission(commands.getString("Invsee.permission"))
                        .async(commands.getBoolean("Invsee.async"))
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
            player.sendMessage("§cUtilize /invsee <player>");
            return;
        }

        Player target = Utils.getPlayer(targetName);
        if(target == null){
            player.sendMessage("§cJogador não encontrado!");
            return;
        }

        player.openInventory(target.getInventory());
        inventories.put(player.getName(), target.getName());
        player.sendMessage("§aInventário de " + target.getName() + " foi aberto!");
    }

}

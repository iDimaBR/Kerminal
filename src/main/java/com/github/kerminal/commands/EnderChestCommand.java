package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@RequiredArgsConstructor
public class EnderChestCommand {

    private final Kerminal plugin;
    public static HashMap<String, String> enderchests = new HashMap<>();

    @Command(
            name = "enderchest",
            aliases = {"echest"},
            permission = "kerminal.enderchest",
            usage = "/echest <jogador>",
            target = CommandTarget.PLAYER
    )
    public void onEnderchest(Context<CommandSender> context, @Optional String targetName) {
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

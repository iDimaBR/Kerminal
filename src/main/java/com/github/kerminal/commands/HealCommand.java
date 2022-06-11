package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

@AllArgsConstructor
public class HealCommand {

    private Kerminal plugin;

    @Command(
            name = "heal",
            aliases = {"vida","regenerar"},
            permission = "kerminal.heal",
            usage = "/heal <jogador>"
    )
    public void onHeal(Context<CommandSender> context, @Optional String targetName) {
        final ConfigUtil messages = plugin.getMessages();
        final int args = context.argsCount();
        final CommandSender sender = context.getSender();

        if(args == 0){
            if(!(sender instanceof Player)){
                sender.sendMessage("§cApenas jogadores podem executar esse comando.");
                return;
            }

            Player player = (Player) sender;
            player.setHealth(20);
            player.sendMessage("§aVida recuperada!");
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if(target == null){
            sender.sendMessage("§cJogador não encontrado!");
            return;
        }

        target.setHealth(20);
        sender.sendMessage("§aVida de " + target.getName() + " foi recuperada!");
    }

}

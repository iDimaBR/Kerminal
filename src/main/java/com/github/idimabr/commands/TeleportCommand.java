package com.github.idimabr.commands;


import com.github.idimabr.Kerminal;
import com.github.idimabr.utils.Mode;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TeleportCommand {

    private Kerminal plugin;

    @Command(
            name = "tp",
            aliases = {"teleport", "teleportar"},
            description = "Teleporta um jogador para outro",
            permission = "kerminal.tp"
    )
    public void Teleport(
            Context<CommandSender> c,
            @Optional String target,
            @Optional String target2
    ) {
        CommandSender sender = c.getSender();

        if (target == null) {
            sender.sendMessage("§cUse: /tp <jogador> ou /tp <jogador> <jogador>");
            return;
        }
        if (target != null) {
            if (target2 != null) {
                Player p = Bukkit.getPlayer(target);
                Player p2 = Bukkit.getPlayer(target2);
                if (p != null) {
                    if (p2 != null) {
                        p.teleport(p2);
                        sender.sendMessage("§aO jogador §f" + p.getName() + "§a foi teleportado para o jogador " + p2.getName());
                        p2.sendMessage("§aO jogador §f" + p.getName() + "§a foi teleportado para você");
                        p.sendMessage("§aVocê foi teleportado para o jogador §f" + p2.getName());
                    } else {
                        sender.sendMessage("§cJogador não encontrado!");
                    }
                } else {
                    sender.sendMessage("§cJogador não encontrado!");
                }
            }
        }
        if (sender instanceof Player) {
            if (target != null) {
                if (target2 == null) {
                    Player p = Bukkit.getPlayer(target);
                    if (p != null) {
                        ((Player) sender).teleport(p);
                        p.sendMessage("§aVocê foi teleportado com sucesso!");
                    } else {
                        sender.sendMessage("§cJogador não encontrado!");
                    }
                }
            }
        } else {
            sender.sendMessage("§cUse: /tp <player> <player>");
        }
    }
}

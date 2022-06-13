package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class FlyCommand {

    private Kerminal plugin;

    @Command(
            name = "fly",
            aliases = {"voar"},
            permission = "kerminal.fly"
    )
    public void onFly(
            Context<CommandSender> context,
            @Optional String target
    ) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) sender;

        if (target != null) {
            final Player targetPlayer = Bukkit.getPlayer(target);

            if (targetPlayer == null) {
                context.sendMessage("§cJogador não encontrado");
                return;
            }
            if (targetPlayer.getAllowFlight()) {
                targetPlayer.setAllowFlight(false);
                context.sendMessage("§c Modo voar do jogador " + targetPlayer.getName() + " foi desativado!");
                targetPlayer.sendMessage("§cModo voar desativado.");
                return;
            } else {
                targetPlayer.setAllowFlight(true);
                context.sendMessage("§a Modo voar do jogador " + targetPlayer.getName() + " foi ativado com sucesso!");
                targetPlayer.sendMessage("§aO Modo voar ativado!");
                return;
            }
        }
        if (sender instanceof Player) {
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                context.sendMessage("§cO Fly foi desativado.");
                return;
            } else {
                player.setAllowFlight(true);
                context.sendMessage("§aO Fly foi ativado.");
                return;
            }
        }
    }

}

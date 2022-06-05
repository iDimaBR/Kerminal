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
public class GamemodeCommand {

    private Kerminal plugin;
    final List<String> tpa = new ArrayList<>();
    @Command(
            name = "gamemode",
            aliases = {"gm"},
            description = "Muda seu gamemode :P",
            permission = "kerminal.gamemode"
    )
    public void onGamemode(Context<CommandSender> c, @Optional String gamemode, @Optional String target) {

        if (gamemode == null) {
            c.sendMessage("§cModo não encontrado, use /gm <modo>");
            return;
        }
        final Mode mode = Mode.of(gamemode);

        if (mode == null) {
            c.sendMessage("§cModo não encontrado, use /gm <modo>");
            return;
        }
        final CommandSender sender = c.getSender();

        if (target != null) {
            final Player targetPlayer = Bukkit.getPlayer(target);

            if (targetPlayer == null) {
                c.sendMessage("§cJogador não encontrado");
                return;
            }
            targetPlayer.setGameMode(mode.getGameMode());
            c.sendMessage("§aModo do jogaodr §f" + targetPlayer.getName() + "§a alterado para §f" + mode.getGameMode());
            targetPlayer.sendMessage("§aSeu modo foi alterado por §f" + sender.getName() + "§a para §f" + mode.getGameMode());
            return;
        }
        if (sender instanceof Player) {
            ((Player) sender).setGameMode(mode.getGameMode());
            ((Player) sender).sendMessage("§aSeu modo para §f" + mode.getGameMode());
            return;
        }
        sender.sendMessage("Use /gm <modo> <jogador>");
    }
}

package com.github.idimabr.commands;


import com.github.idimabr.Kerminal;
import com.github.idimabr.utils.HelpGui;
import com.github.idimabr.utils.Mode;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CommandsKerminal {

    private Kerminal plugin;

    @Command(
            name = "ajuda",
            aliases = {"help.kerminal", "ajuda.kerminal"},
            description = "Mostra tudo :P"
    )
    public void Help(Context<CommandSender> c)
    {
        Player p = (Player) c.getSender();

        if (p instanceof Player) {

            p.openInventory(HelpGui.getHelpInventory());
        }
    }

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
    @Command(
            name = "gamemode",
            aliases = {"gm"},
            description = "Muda seu gamemode :P",
            permission = "kerminal.gamemode"
    )
    public void onGamemode(
            Context<CommandSender> c,
            @Optional String gamemode,
            @Optional String target
            )
    {
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
            ((Player)sender).setGameMode(mode.getGameMode());
            ((Player)sender).sendMessage("§aSeu modo para §f" + mode.getGameMode());
            return;
        }
        sender.sendMessage("Use /gm <modo> <jogador>");
    }
}

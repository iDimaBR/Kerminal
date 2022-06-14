package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class TeleportCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public TeleportCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Teleport.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Teleport.command"))
                        .aliases(commands.getStringList("Teleport.aliases").toArray(new String[0]))
                        .permission(commands.getString("Teleport.permission"))
                        .async(commands.getBoolean("Teleport.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0), context.getArg(0));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> c, @Optional String target, @Optional String target2) {

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

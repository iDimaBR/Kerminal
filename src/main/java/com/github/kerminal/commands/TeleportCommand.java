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

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Teleport";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public TeleportCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
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

    public void register(){
        if (!commands.getBoolean(identifierCommand + ".enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(command)
                        .aliases(aliases)
                        .permission(permission)
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0), context.getArg(1));
                    return false;
                }
        );
    }
}

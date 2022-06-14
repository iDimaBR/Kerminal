package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TpaCommand {

    private Kerminal plugin;
    final List<String> tpa = new ArrayList<>();
    private ConfigUtil commands;

    public TpaCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Tpa.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Tpa.command"))
                        .aliases(commands.getStringList("Tpa.aliases").toArray(new String[0]))
                        .permission(commands.getString("Tpa.permission"))
                        .async(commands.getBoolean("Tpa.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> c, @Optional String target) {
        //TODO: Delay para fazer outro tpa
        if (target == null) {
            c.sendMessage("§cUse: /tpa <jogador>");
            return;
        }
        final Player targetPlayer = Bukkit.getPlayer(target);

        if (targetPlayer == null) {
            c.sendMessage("§cJogador não encontrado");
            return;
        }
        final Player sender = (Player) c.getSender();
        if (tpa.contains(sender.getName())) {
            c.sendMessage("§cUsuário ja possui um pedido!");
            //TODO Delay para o usuario aceitar o pedido no tpaccept
            return;
        }
        tpa.add(targetPlayer.getName());
        targetPlayer.sendMessage("§eO jogador §f" + sender.getName() + "§e te envieu um pedido de teleporte, use /tpaceitar <jogador> para aceitar");
        sender.sendMessage("§aEnviado pedido de teleporte para §f" + targetPlayer.getName());

    }

    @Command(
            name = "tpaceitar",
            aliases = {"tpaaceitar", "tpaa"},
            permission = "kerminal.tpa",
            target = CommandTarget.PLAYER
    )
    public void onTpaccept(
            Context<CommandSender> c,
            @Optional String target
    ) {
        CommandSender sender = c.getSender();

        if (target == null) {
            sender.sendMessage("§cUse: /tpaceitar <jogador>");
            return;
        }
        final Player p = Bukkit.getPlayer(target);

        if (p == null) {
            sender.sendMessage("§cJogador não encontrado!");
            return;
        }
        if (p != null) {
            if (sender instanceof Player) {
                if (tpa.contains(p.getName())) {
                    tpa.remove(p.getName());
                    sender.sendMessage("§aVocê aceitou o pedido de teleporte de §f" + p.getName());
                    p.sendMessage("§aO jogador §f" + sender.getName() + "§a aceitou seu pedido de teleporte");
                    p.teleport((Player) sender);
                } else {
                    sender.sendMessage("§cJogador não te pediu uma requisição de teleporte!");
                }
            }
        }
    }
    @Command(
            name = "tpadeny",
            aliases = {"tpade", "tpdeny", "tpdenied", "tpdenyed", "tparecusar", "tpadeny"},
            permission = "kerminal.tpa",
            target = CommandTarget.PLAYER
    )
    public void onTpadeny(
            Context<CommandSender> c,
            @Optional String target
    ) {
        CommandSender sender = c.getSender();

        if (target == null) {
            sender.sendMessage("§cUse: /tpadeny <jogador>");
            return;
        }
        final Player p = Bukkit.getPlayer(target);

        if (p == null) {
            sender.sendMessage("§cJogador não encontrado!");
            return;
        }
        if (p != null) {
            if (sender instanceof Player) {
                if (tpa.contains(p.getName())) {
                    tpa.remove(p.getName());
                    sender.sendMessage("§aVocê recusou o pedido de teleporte de §f" + p.getName());
                    p.sendMessage("§aO jogador §f" + sender.getName() + "§a recusou seu pedido de teleporte");
                } else {
                    sender.sendMessage("§cJogador não te pediu uma requisição de teleporte!");
                }
            }
        }
    }
}

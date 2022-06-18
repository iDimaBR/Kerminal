package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class ColorsCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public ColorsCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Colors.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Colors.command"))
                        .aliases(commands.getStringList("Colors.aliases").toArray(new String[0]))
                        .permission(commands.getString("Colors.permission"))
                        .async(commands.getBoolean("Colors.async"))
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        Player player = (Player) context.getSender();
        player.sendMessage("§7Lista de cores:");
        player.sendMessage("");
        player.sendMessage("    §0&0 §7- §0preto");
        player.sendMessage("    §1&1 §7- §1azul escuro");
        player.sendMessage("    §2&2 §7- §2verde escuro");
        player.sendMessage("    §3&3 §7- §3ciano");
        player.sendMessage("    §4&4 §7- §4vermelho");
        player.sendMessage("    §5&5 §7- §5roxo");
        player.sendMessage("    §6&6 §7- §6dourado");
        player.sendMessage("    §7&7 §7- §7cinza");
        player.sendMessage("    §8&8 §7- §8cinza escuro");
        player.sendMessage("    §9&9 §7- §9magenta");
        player.sendMessage("    §a&a §7- §averde");
        player.sendMessage("    §b&b §7- §bazul");
        player.sendMessage("    §c&c §7- §cvermelho claro");
        player.sendMessage("    §d&d §7- §drosa");
        player.sendMessage("    §e&e §7- §eamarelo");
        player.sendMessage("    §f&f §7- §fbranco");
        player.sendMessage("    §f&k §7- §f§kofuscado");
        player.sendMessage("    §f&l §7- §f§lnegrito");
        player.sendMessage("    §f&m §7- §f§mriscado");
        player.sendMessage("    §f&n §7- §f§nSublinhado");
        player.sendMessage("    §f&o §7- §f§oItálico");

    }

}

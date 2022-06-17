package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class NickCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public NickCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Nick.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Nick.command"))
                        .aliases(commands.getStringList("Nick.aliases").toArray(new String[0]))
                        .permission(commands.getString("Nick.permission"))
                        .async(commands.getBoolean("Nick.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context, String nick) {
        final ConfigUtil messages = plugin.getMessages();
        Player player = (Player) context.getSender();

        player.setPlayerListName(nick);
        player.setDisplayName(nick);
        player.setCustomName(nick);
        player.sendMessage("§aVocê se camuflou como: §f" + nick);
    }
}

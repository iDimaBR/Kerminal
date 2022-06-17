package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class OnlineCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public OnlineCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Onlines.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Onlines.command"))
                        .aliases(commands.getStringList("Onlines.aliases").toArray(new String[0]))
                        .permission(commands.getString("Onlines.permission"))
                        .async(commands.getBoolean("Onlines.async"))
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();

        context.sendMessage("§aJogadores: §f" + Bukkit.getOnlinePlayers().size() + " onlines");
    }

}

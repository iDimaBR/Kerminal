package com.github.kerminal.commands;

import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.storage.dao.StorageRepository;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TeleportAllCommand {
    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Tpall";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public TeleportAllCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }
    public void onCommand(Context<CommandSender> context
    ) {
        final Player player = (Player) context.getSender();
        final LangController messages = plugin.getLangController();

        player.sendMessage(messages.getString("Tpall.Sucess"));

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player p : players) {
            p.teleport(player);
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
                    onCommand(context);
                    return false;
                }
        );
    }
}

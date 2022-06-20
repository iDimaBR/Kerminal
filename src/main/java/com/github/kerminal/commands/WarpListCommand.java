package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.models.Warp;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public class WarpListCommand {

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "WarpList";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public WarpListCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context) {
        final LangController messages = plugin.getLangController();
        final Player player = (Player) context.getSender();
        final Map<String, Warp> warpsList = plugin.getWarpsList();

        player.sendMessage(messages.getString("Commands.WarpSection.WarpList.Listing").replace("%warps%", (warpsList.size() == 0 ? "Nenhum" : StringUtils.join(warpsList.keySet(), "§7,§f "))));
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

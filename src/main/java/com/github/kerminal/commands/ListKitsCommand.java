package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.KitController;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.models.Kit;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.TimeUtils;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ListKitsCommand {

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "ListKits";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public ListKitsCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context) {
        final LangController messages = plugin.getLangController();
        final KitController kitController = plugin.getKitController();
        final CommandSender sender = context.getSender();

        final Set<String> listKits = kitController.getLoadedKits().keySet();
        sender.sendMessage(messages.getString("Commands.KitSection.ListKits.Listing").replace("%kits%", (listKits.isEmpty() ? "Nenhum" : StringUtils.join(listKits, ", "))));
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

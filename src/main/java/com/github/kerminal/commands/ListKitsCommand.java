package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.KitController;
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

    public ListKitsCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("ListKits.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("ListKits.command"))
                        .aliases(commands.getStringList("ListKits.aliases").toArray(new String[0]))
                        .permission(commands.getString("ListKits.permission"))
                        .async(commands.getBoolean("ListKits.async"))
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        final KitController kitController = plugin.getKitController();
        final CommandSender sender = context.getSender();

        final Set<String> listKits = kitController.getLoadedKits().keySet();
        sender.sendMessage("§aKits: §f" + (listKits.isEmpty() ? "Nenhum" : StringUtils.join(listKits, ", ")));
    }
}

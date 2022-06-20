package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.KitController;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Serializer;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
public class DeleteKitCommand {

    private Kerminal plugin;
    private ConfigUtil commands;
    private ConfigUtil kits;
    private final String identifierCommand = "DeleteKit";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public DeleteKitCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.kits = plugin.getKits();
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, String name) {
        final LangController messages = plugin.getLangController();
        Player player = (Player) context.getSender();
        final KitController kitController = plugin.getKitController();

        if(!kitController.existsKit(name)){
            player.sendMessage(messages.getString("Commands.KitSection.NoExistKit").replace("%name%", name));
            return;
        }

        kits.set(name, null);
        kits.save();
        kitController.getLoadedKits().remove(name);
        player.sendMessage(messages.getString("Commands.KitSection.DeleteKit.Success").replace("%name%", name));

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
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

}

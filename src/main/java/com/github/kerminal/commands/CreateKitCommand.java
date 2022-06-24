package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.KitController;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Serializer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CreateKitCommand {

    private Kerminal plugin;
    private ConfigUtil commands;
    private ConfigUtil kits;
    private final String identifierCommand = "CreateKit";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public CreateKitCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.kits = plugin.getKits();
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, String name, String delay) {
        final LangController messages = plugin.getLangController();
        final Player player = (Player) context.getSender();
        final KitController kitController = plugin.getKitController();
        final int argsCount = context.argsCount();

        if(argsCount == 0){
            player.sendMessage(messages.getString("Commands.KitSection.CreateKit.Usage").replace("%command%", command));
            return;
        }

        if(!NumberUtils.isNumber(delay)){
            player.sendMessage(messages.getString("Commands.KitSection.CreateKit.InvalidTime"));
            return;
        }

        if(kits.contains(name.toLowerCase())){
            player.sendMessage(messages.getString("Commands.KitSection.CreateKit.ExistKit").replace("%name%", name));
            return;
        }

        ItemStack[] contents = player.getInventory().getContents();
        if(Arrays.stream(contents).noneMatch(Objects::nonNull)){
            player.sendMessage(messages.getString("Commands.KitSection.CreateKit.NoHaveItem"));
            return;
        }

        kits.set(name + ".itens", Serializer.itemStackArrayToBase64(contents));
        kits.set(name + ".delay", Integer.parseInt(delay));
        kits.save();
        kitController.registerKit(name);
        player.sendMessage(messages.getString("Commands.KitSection.CreateKit.Success").replace("%name%", name));

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
                    onCommand(context, context.getArg(0), context.getArg(0));
                    return false;
                }
        );
    }

}

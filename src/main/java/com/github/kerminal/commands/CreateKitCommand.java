package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.KitController;
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
import java.util.stream.Collectors;

@AllArgsConstructor
public class CreateKitCommand {

    private Kerminal plugin;
    private ConfigUtil commands;
    private ConfigUtil kits;

    public CreateKitCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.kits = plugin.getKits();
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("CreateKit.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("CreateKit.command"))
                        .aliases(commands.getStringList("CreateKit.aliases").toArray(new String[0]))
                        .permission(commands.getString("CreateKit.permission"))
                        .async(commands.getBoolean("CreateKit.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0), context.getArg(1));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context, String name, String delay) {
        final ConfigUtil messages = plugin.getMessages();
        Player player = (Player) context.getSender();
        final KitController kitController = plugin.getKitController();

        if(!NumberUtils.isNumber(delay)){
            player.sendMessage("§cColoque um tempo válido no kit!");
            player.sendMessage("§cPara definir sem tempo, coloque: 0");
            return;
        }

        if(kits.contains(name.toLowerCase())){
            player.sendMessage("§cO Kit '" + name + "' já existe!");
            return;
        }

        kits.set(name + ".itens", Serializer.itemStackArrayToBase64(player.getInventory().getContents()));
        kits.set(name + ".delay", Integer.parseInt(delay));
        kits.save();
        kitController.loadAllKits();
        player.sendMessage("§aKit '" + name + "' foi criado!");
    }

}

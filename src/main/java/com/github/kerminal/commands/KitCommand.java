package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.KitController;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.models.Kit;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Serializer;
import com.github.kerminal.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class KitCommand {

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final ConfigUtil kits;
    private final String identifierCommand = "Kit";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public KitCommand(Kerminal plugin) {
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
        final int argsCount = context.argsCount();

        if(argsCount == 0){
            player.sendMessage(messages.getString("Commands.KitSection.Kit.Usage").replace("%command%", command));
            return;
        }
        name = name.toLowerCase();

        if(!kitController.existsKit(name)){
            player.sendMessage(messages.getString("Commands.KitSection.NoExistKit").replace("%name%", name));
            return;
        }

        if(!context.testPermission(permission + "." + name.toLowerCase(), false)) return;

        final Kit kit = kitController.getKit(name);
        final ItemStack[] itens = kit.getItens();
        final int delay = kit.getDelay();
        final Map<UUID, Long> delayKit = kit.getCooldownMap();

        if(delayKit.containsKey(player.getUniqueId()) && delayKit.get(player.getUniqueId()) > System.currentTimeMillis()) {
            player.sendMessage(messages.getString("DefaultCallback.WaitingDelay").replace("%delay%", TimeUtils.format(delayKit.get(player.getUniqueId()) - System.currentTimeMillis())));
            return;
        }

        if(!hasSpace(player, kit.getNeedSlots())){
            player.sendMessage(messages.getString("Commands.KitSection.Kit.NoHaveSpace"));
            return;
        }

        for (ItemStack item : itens) {
            if(item == null) continue;
            player.getInventory().addItem(item);
        }

        player.sendMessage(messages.getString("Commands.KitSection.Kit.Success").replace("%name%", kit.getName()));

        final long millis = System.currentTimeMillis() + (1000L * delay);
        delayKit.put(player.getUniqueId(), millis);
        plugin.getRepository().insertDelayKit(player, name, millis);
    }

    private boolean hasSpace(Player player, int needed){
        return Arrays.stream(player.getInventory().getContents()).filter(Objects::isNull).count() >= needed;
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

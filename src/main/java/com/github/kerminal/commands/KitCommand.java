package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.KitController;
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

    private final Map<UUID, Long> delayKit = new HashMap<>();

    public KitCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.kits = plugin.getKits();
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Kit.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Kit.command"))
                        .aliases(commands.getStringList("Kit.aliases").toArray(new String[0]))
                        .permission(commands.getString("Kit.permission"))
                        .async(commands.getBoolean("Kit.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context, String name) {
        final ConfigUtil messages = plugin.getMessages();
        Player player = (Player) context.getSender();
        final KitController kitController = plugin.getKitController();

        if(!kitController.existsKit(name)){
            player.sendMessage("§cO Kit '" + name + "' não existe!");
            return;
        }

        final Long delayValue = delayKit.getOrDefault(player.getUniqueId(), 0L);
        if(delayValue > System.currentTimeMillis()) {
            player.sendMessage("§cVocê ainda precisa esperar " + TimeUtils.format(delayValue - System.currentTimeMillis()));
            return;
        }

        final Kit kit = kitController.getKit(name);
        final ItemStack[] itens = kit.getItens();
        final int delay = kit.getDelay();

        if(!hasSpace(player, kit.getNeedSlots())){
            player.sendMessage("§cVocê não tem espaço suficiente no inventário para receber o Kit.");
            return;
        }

        for (ItemStack item : itens) {
            if(item == null) continue;
            player.getInventory().addItem(item);
        }

        player.sendMessage("§aVocê recebeu o kit '" + name + "'.");
        delayKit.put(player.getUniqueId(), System.currentTimeMillis() + (1000L * delay));
    }

    private boolean hasSpace(Player player, int needed){
        return Arrays.stream(player.getInventory().getContents()).filter(Objects::isNull).count() >= needed;
    }
}

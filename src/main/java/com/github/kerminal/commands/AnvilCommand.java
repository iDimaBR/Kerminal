package com.github.kerminal.commands;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@AllArgsConstructor
public class AnvilCommand {
    private Kerminal plugin;

    private ConfigUtil commands;

    private final String identifierCommand = "Anvil";

    private final String command;

    private final String[] aliases;

    private final String permission;

    public AnvilCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }
    public void onCommand(Context<CommandSender> context) {
        Player player = (Player) context.getSender();
        Inventory anvil = Bukkit.createInventory(null, InventoryType.ANVIL, "Bigorna de " + player.getName());

        player.openInventory(anvil);
        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
        player.sendMessage("§aBigorna aberta!");
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

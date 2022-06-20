package com.github.kerminal.commands;

import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Enchants;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
public class EnchantsCommand {

    private Kerminal plugin;
    private ConfigUtil commands;
    private final String identifierCommand = "Enchant";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public EnchantsCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, @Optional String enchant, @Optional String level) {
        final CommandSender sender = context.getSender();
        final LangController messages = plugin.getLangController();
        final Player player = (Player) sender;
        final Enchants enchantment = Enchants.of(enchant);

        int args = context.argsCount();

        if(args < 2){
            context.sendMessage(messages.getString("Commands.Enchant.Usage").replace("%command%", command));
            return;
        }

        if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) == null) {
            context.sendMessage(messages.getString("DefaultCallback.HandEmpty"));
            return;
        }
        if (enchantment == null) {
            for (String string : messages.getStringList("Commands.Enchant.NotFound")) {
                player.sendMessage(string.replace("%enchantments%", StringUtils.join(Arrays.stream(Enchantment.values()).map(Enchantment::getName).collect(Collectors.toList()), ", ")));
            }
            return;
        }
        if(!NumberUtils.isNumber(level)){
            context.sendMessage(messages.getString("Commands.Enchant.Levelinvalid"));
            return;
        }

        player.getItemInHand().addUnsafeEnchantment(enchantment.getEnchantment(), Integer.parseInt(level));
        context.sendMessage(messages.getString("Commands.Enchant.Success").replace("%name%", enchantment.name()));
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

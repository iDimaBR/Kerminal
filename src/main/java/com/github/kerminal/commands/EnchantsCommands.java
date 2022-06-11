package com.github.kerminal.commands;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Enchants;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Completer;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
public class EnchantsCommands {

    private Kerminal plugin;

    @Command(
            name = "enchant",
            aliases = {"encantar"},
            permission = "kerminal.enchant",
            target = CommandTarget.PLAYER
    )
    public void onEnchant(Context<CommandSender> context, @Optional String enchant, @Optional String level) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) sender;
        final Enchants enchantment = Enchants.of(enchant);

        int args = context.argsCount();

        if(args < 2){
            context.sendMessage("§cUtilize /encantar <enchantamento> <nivel>");
            return;
        }

        if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) == null) {
            context.sendMessage("§cVocê não possui um item na mão.");
            return;
        }
        if (enchantment == null) {
            context.sendMessage("§cEncantamento não encontrado!");
            context.sendMessage("§aEncantamentos: §f" + StringUtils.join(Arrays.stream(Enchantment.values()).map(Enchantment::getName).collect(Collectors.toList()), ", "));
            return;
        }
        if(!NumberUtils.isNumber(level)){
            context.sendMessage("§cO nível do encantamento é inválido!");
            return;
        }

        player.getItemInHand().addUnsafeEnchantment(enchantment.getEnchantment(), Integer.parseInt(level));
        context.sendMessage("§aO encantamento§f " + enchantment.name() + " §afoi adicionado.");
    }

}

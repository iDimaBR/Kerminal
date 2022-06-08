package com.github.kerminal.commands;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Enchants;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class EnchantsCommands {

    private Kerminal plugin;

    @Command(
            name = "encantar",
            aliases = {"enchant"},
            permission = "kerminal.enchant",
            target = CommandTarget.PLAYER
    )
    public void onEnchant(
            Context<CommandSender> context,
            @Optional String enchant,
            @Optional String level

    ) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) sender;
        final Enchants enchantment = Enchants.of(enchant);
        final Integer minLevel = 0;


        if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) == null) {
            context.sendMessage("§cVocê não possui um item na mão.");
            return;

        } else {
            if (enchantment == null) {
                context.sendMessage("§cEncantamento não encontrado, encantamentos disponiveis; ");
                //TODO: PUT ALL ENCHANTS IN A LIST IN TEXTCOMPONENT WITH HOVEREVENT AND CLICKEVENT
                return;
            }
            if (level.equalsIgnoreCase("0")) {
                player.getItemInHand().removeEnchantment(enchantment.getEnchantment());
                context.sendMessage("§aO encantamento§f " + enchantment.name() + " §afoi removido do item.");
                return;
            }
            if (level.startsWith("-")) {
                context.sendMessage("§cO nivel não pode ser negativo");
                return;
            }
            if (level.equalsIgnoreCase("null")) {
                context.sendMessage("§cO nivel não pode ser nulo");
                return;
            }
            if (level == null) {
                context.sendMessage("§cNível não encontrado.");
                return;
            }
            if (enchantment != null) {
                if (level != null) {
                    player.getItemInHand().addUnsafeEnchantment(enchantment.getEnchantment(), Integer.parseInt(level));
                    context.sendMessage("§aO encantamento§f " + enchantment.name() + " §afoi adicionado ao item, com o nível§f " + level + "§a.");
                }
            }
        }
    }
}

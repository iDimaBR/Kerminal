package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@AllArgsConstructor
public class HatCommand {

    private Kerminal plugin;

    @Command(
            name = "hat",
            aliases = {"chapeu"},
            permission = "kerminal.hat",
            usage = "/hat <jogador>"
    )
    public void onHat(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        final CommandSender sender = context.getSender();

        if(!(sender instanceof Player)){
            sender.sendMessage("§cApenas jogadores podem executar esse comando.");
            return;
        }

        Player player = (Player) sender;
        if(!applyHat(player)){
            player.sendMessage("§cVocê precisa segurar algo em suas maõs");
            return;
        }

        player.sendMessage("§aAproveite seu chapeu novo :3");
    }

    private boolean applyHat(Player player){
        final PlayerInventory inventory = player.getInventory();
        final ItemStack itemInHand = inventory.getItemInHand();
        final ItemStack helmet = inventory.getHelmet();

        if(itemInHand.getType() == Material.AIR) return false;

        inventory.setItemInHand(null);
        if(helmet != null)
            inventory.setItemInHand(helmet);

        inventory.setHelmet(itemInHand);
        return true;
    }

}

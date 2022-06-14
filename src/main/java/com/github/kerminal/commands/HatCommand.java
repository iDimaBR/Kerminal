package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
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
    private ConfigUtil commands;

    public HatCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Hat.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Hat.command"))
                        .aliases(commands.getStringList("Hat.aliases").toArray(new String[0]))
                        .permission(commands.getString("Hat.permission"))
                        .async(commands.getBoolean("Hat.async"))
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context) {
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

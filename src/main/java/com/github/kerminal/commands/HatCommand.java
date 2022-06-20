package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
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

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Hat";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public HatCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context) {
        final LangController messages = plugin.getLangController();
        final CommandSender sender = context.getSender();

        if(!(sender instanceof Player)){
            sender.sendMessage("§cApenas jogadores podem executar esse comando.");
            return;
        }

        Player player = (Player) sender;
        if(!applyHat(player)){
            player.sendMessage(messages.getString("DefaultCallback.HandEmpty"));
            return;
        }

        player.sendMessage(messages.getString("Commands.Hat.Success"));
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

package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class ClearCommand {

    private Kerminal plugin;

    @Command(
            name = "clear",
            aliases = {"limpar"},
            permission = "kerminal.clear",
            usage = "/clear <jogador>"
    )
    public void onFeed(Context<CommandSender> context, @Optional String targetName) {
        final ConfigUtil messages = plugin.getMessages();
        final int args = context.argsCount();
        final CommandSender sender = context.getSender();

        if(args == 0){
            if(!(sender instanceof Player)){
                sender.sendMessage("§cApenas jogadores podem executar esse comando.");
                return;
            }

            Player player = (Player) sender;
            player.getInventory().setArmorContents(new ItemStack[0]);
            player.getInventory().clear();
            player.sendMessage("§aInventário limpo!");
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if(target == null){
            sender.sendMessage("§cJogador não encontrado!");
            return;
        }

        target.getInventory().setArmorContents(new ItemStack[0]);
        target.getInventory().clear();
        sender.sendMessage("§aInventário de " + target.getName() + " foi limpo!");
    }

}

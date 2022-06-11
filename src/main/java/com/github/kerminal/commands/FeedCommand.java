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

@AllArgsConstructor
public class FeedCommand {

    private Kerminal plugin;

    @Command(
            name = "feed",
            aliases = {"fome","comida"},
            permission = "kerminal.feed",
            usage = "/feed <jogador>"
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
            player.setFoodLevel(20);
            player.sendMessage("§aFome recuperada!");
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if(target == null){
            sender.sendMessage("§cJogador não encontrado!");
            return;
        }

        target.setFoodLevel(20);
        sender.sendMessage("§aFome de " + target.getName() + " foi recuperada!");
    }

}

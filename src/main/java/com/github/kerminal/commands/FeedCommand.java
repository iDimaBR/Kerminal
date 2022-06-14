package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class FeedCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public FeedCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Feed.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Feed.command"))
                        .aliases(commands.getStringList("Feed.aliases").toArray(new String[0]))
                        .permission(commands.getString("Feed.permission"))
                        .async(commands.getBoolean("Feed.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context, @Optional String targetName) {
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

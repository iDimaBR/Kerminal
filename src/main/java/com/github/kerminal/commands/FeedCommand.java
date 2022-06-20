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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class FeedCommand {

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Feed";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public FeedCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, @Optional String targetName) {
        final LangController messages = plugin.getLangController();
        final int args = context.argsCount();
        final CommandSender sender = context.getSender();

        if(args == 0){
            if(!(sender instanceof Player)){
                sender.sendMessage(messages.getString("DefaultCallback.OnlyPlayers"));
                return;
            }

            Player player = (Player) sender;
            player.setFoodLevel(20);
            player.sendMessage(messages.getString("Commands.Feed.Success"));
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if(target == null){
            sender.sendMessage(messages.getString("DefaultCallback.PlayerNotFound"));
            return;
        }

        target.setFoodLevel(20);
        sender.sendMessage(messages.getString("Commands.Feed.SuccessOther").replace("%target%", targetName));
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
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

}

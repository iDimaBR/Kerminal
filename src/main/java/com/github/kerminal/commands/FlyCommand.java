package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class FlyCommand {

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Fly";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public FlyCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, @Optional String target) {
        final CommandSender sender = context.getSender();
        final LangController messages = plugin.getLangController();
        final Player player = (Player) sender;

        if (target != null) {
            final Player targetPlayer = Bukkit.getPlayer(target);

            if (targetPlayer == null) {
                context.sendMessage(messages.getString("DefaultCallback.PlayerNotFound"));
                return;
            }
            if (targetPlayer.getAllowFlight()) {
                targetPlayer.setAllowFlight(false);
                context.sendMessage(messages.getString("Commands.Fly.SuccessOther-Off"));
                targetPlayer.sendMessage(messages.getString("Commands.Fly.Success-Off"));
                return;
            } else {
                targetPlayer.setAllowFlight(true);
                context.sendMessage(messages.getString("Commands.Fly.SuccessOther-On"));
                targetPlayer.sendMessage(messages.getString("Commands.Fly.Success-On"));
                return;
            }
        }
        if (sender instanceof Player) {
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                context.sendMessage(messages.getString("Commands.Fly.Success-Off"));
                return;
            } else {
                player.setAllowFlight(true);
                context.sendMessage(messages.getString("Commands.Fly.Success-On"));
                return;
            }
        }
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

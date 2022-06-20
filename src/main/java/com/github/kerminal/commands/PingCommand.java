package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PingCommand {

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Ping";
    private final String command;
    private final String[] aliases;
    private final String permission;
    public PingCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, @Optional String target) {
        final CommandSender sender = context.getSender();
        final LangController messages = plugin.getLangController();
        final int args = context.argsCount();
        final Player player = (Player) sender;

        if(args == 0){
            player.sendMessage(messages.getString("Commands.Ping.Success").replace("%ping%", getPing(player)+"ms"));
            return;
        }

        if (args == 1){
            final Player targetPlayer = Bukkit.getPlayer(target);
            if (targetPlayer == null) {
                context.sendMessage(messages.getString("DefaultCallback.PlayerNotFound"));
                return;
            }

            player.sendMessage(messages.getString("Commands.Ping.SuccessOther").replace("%target%", targetPlayer.getName()).replace("%ping%", getPing(player)+"ms"));
        }
    }

    public static int getPing(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        return entityPlayer.ping;
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

package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PingCommand {

    private Kerminal plugin;

    @Command(
            name = "ping",
            aliases = {"pingar"},
            permission = "kerminal.ping",
            target = CommandTarget.PLAYER
    )
    public void onPing(
            Context<CommandSender> context,
            @Optional String target
    ) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final int args = context.argsCount();
        final Player player = (Player) sender;

        if(args == 0){
            player.sendMessage("§aSeu ping é §f" + getPing(player) + "ms");
            return;
        }

        if (args == 1){
            final Player targetPlayer = Bukkit.getPlayer(target);
            if (targetPlayer == null) {
                context.sendMessage("§cJogador não encontrado");
                return;
            }

            player.sendMessage("§aO ping de " + target + " é §f" + getPing(targetPlayer) + "ms");
        }
    }

    public static int getPing(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        return entityPlayer.ping;
    }

}

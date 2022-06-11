package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CraftCommand {

    private Kerminal plugin;

    @Command(
            name = "craft",
            aliases = {"craftingtable","ctable"},
            permission = "kerminal.craft",
            target = CommandTarget.PLAYER
    )
    public void onCraft(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        Player player = (Player) context.getSender();
        player.openWorkbench(player.getLocation(), true);
    }

}

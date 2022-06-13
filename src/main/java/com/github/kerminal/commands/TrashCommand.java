package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@AllArgsConstructor
public class TrashCommand {

    private Kerminal plugin;

    @Command(
            name = "lixo",
            aliases = {"lixeira","reciclar"},
            permission = "kerminal.lixo",
            target = CommandTarget.PLAYER
    )
    public void onTrash(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        Player player = (Player) context.getSender();

        player.openInventory(Bukkit.createInventory(null, 6 * 9, "Lixeira"));
    }

}

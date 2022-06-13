package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@AllArgsConstructor
public class LightCommand {

    private Kerminal plugin;

    @Command(
            name = "luz",
            aliases = {"light","iluminar"},
            permission = "kerminal.luz",
            target = CommandTarget.PLAYER
    )
    public void onLight(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        Player player = (Player) context.getSender();

        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.sendMessage("§cLuz desativada!");
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 90), true);
        player.sendMessage("§aLuz ativada!");
    }

}

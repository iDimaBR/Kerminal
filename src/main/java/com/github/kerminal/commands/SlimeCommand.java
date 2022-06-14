package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

@AllArgsConstructor
public class SlimeCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public SlimeCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Slime.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Slime.command"))
                        .aliases(commands.getStringList("Slime.aliases").toArray(new String[0]))
                        .permission(commands.getString("Slime.permission"))
                        .async(commands.getBoolean("Slime.async"))
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        Player player = (Player) context.getSender();

        player.sendMessage("§a§lSLIME> §7Você " + (isSlimeChunk(player) ? "§aESTÁ" : "§cNÃO ESTÁ") + " §7em uma slime chunk.");
    }

    public boolean isSlimeChunk(Player player) {
        final Chunk chunk = player.getLocation().getChunk();
        final long worldSeed = chunk.getWorld().getSeed();
        final int x = chunk.getX();
        final int z = chunk.getZ();
        final Random random = new Random(worldSeed + (x * x * 4987142) + (x * 5947611) + (z * z) * 4392871L + (z * 389711) ^ 0x3AD8025FL);
        return (random.nextInt(10) == 0);
    }
}

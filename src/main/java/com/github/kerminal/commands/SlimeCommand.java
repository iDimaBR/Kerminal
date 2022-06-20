package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
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

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Slime";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public SlimeCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context) {
        final LangController messages = plugin.getLangController();
        Player player = (Player) context.getSender();

        player.sendMessage(messages.getString("Commands.Slime.Success").replace("%status%", (isSlimeChunk(player) ? messages.getString("Commands.Slime.Status-True") : messages.getString("Commands.Slime.Status-False"))));
    }

    public boolean isSlimeChunk(Player player) {
        final Chunk chunk = player.getLocation().getChunk();
        final long worldSeed = chunk.getWorld().getSeed();
        final int x = chunk.getX();
        final int z = chunk.getZ();
        final Random random = new Random(worldSeed + (x * x * 4987142) + (x * 5947611) + (z * z) * 4392871L + (z * 389711) ^ 0x3AD8025FL);
        return (random.nextInt(10) == 0);
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
                    onCommand(context);
                    return false;
                }
        );
    }
}

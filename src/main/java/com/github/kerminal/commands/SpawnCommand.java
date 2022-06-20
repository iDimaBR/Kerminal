package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.DataController;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@AllArgsConstructor
public class SpawnCommand {

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Spawn";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public SpawnCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context) {
        final LangController messages = plugin.getLangController();
        Player player = (Player) context.getSender();
        final DataController controller = plugin.getController();
        final TeleportRegistry teleportRegistry = controller.getRegistryTeleport();

        final Location spawn = plugin.getSpawn();
        if(spawn == null){
            player.sendMessage(messages.getString("Commands.SpawnSection.Spawn.NotDefined"));
            return;
        }

        if (player.hasPermission(permission + ".delay.bypass")) {
            teleportRegistry.teleport(player, spawn);
            return;
        }

        teleportRegistry.register(player, spawn, false);
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

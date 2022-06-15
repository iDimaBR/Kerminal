package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.DataController;
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

    private Kerminal plugin;
    private ConfigUtil commands;

    public SpawnCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Spawn.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Spawn.command"))
                        .aliases(commands.getStringList("Spawn.aliases").toArray(new String[0]))
                        .permission(commands.getString("Spawn.permission"))
                        .async(commands.getBoolean("Spawn.async"))
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
        final DataController controller = plugin.getController();
        final TeleportRegistry teleportRegistry = controller.getRegistry();

        final Location spawn = plugin.getSpawn();
        if(spawn == null){
            player.sendMessage("§cO spawn não foi definido corretamente.");
            return;
        }

        if (player.hasPermission(commands.getString("Spawn.permission") + ".delay.bypass")) {
            player.sendMessage("§aTeleportado!");
            player.teleport(spawn);
            return;
        }

        teleportRegistry.register(player, spawn);
        player.sendMessage("§aTeleportando em " + teleportRegistry.getDelay(player) + " segundos.");
    }

}

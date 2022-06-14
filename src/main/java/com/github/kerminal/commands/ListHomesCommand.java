package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.DataController;
import com.github.kerminal.models.Home;
import com.github.kerminal.models.PlayerData;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

@AllArgsConstructor
public class ListHomesCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public ListHomesCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Listhome.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Listhome.command"))
                        .aliases(commands.getStringList("Listhome.aliases").toArray(new String[0]))
                        .permission(commands.getString("Listhome.permission"))
                        .async(commands.getBoolean("Listhome.async"))
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) sender;
        final DataController controller = plugin.getController();

        PlayerData data = controller.getDataPlayer(player.getUniqueId());
        if(data == null){
            player.sendMessage("§cSuas informações não foram carregadas, entre novamente no servidor.");
            return;
        }

        final Set<String> homes = data.getHomes().keySet();

        player.sendMessage("§aSuas casas: §f" + (homes.size() == 0 ? "Nenhuma" : StringUtils.join(homes.toArray(), "§7,§f ")));
        if(data.getDefaultHome() == null)
            player.sendMessage("§cObs: §7Defina sua casa principal com §f/sethome padrao");
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
    }
}

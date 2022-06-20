package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.DataController;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.customevents.PlayerBackEvent;
import com.github.kerminal.models.Home;
import com.github.kerminal.models.PlayerData;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class BackCommand {

    private Kerminal plugin;
    private ConfigUtil commands;
    private final String identifierCommand = "Back";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public BackCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context) {
        final CommandSender sender = context.getSender();
        final LangController messages = plugin.getLangController();
        final Player player = (Player) sender;
        final DataController controller = plugin.getController();

        PlayerData data = controller.getDataPlayer(player.getUniqueId());
        if(data.getLastLocation() == null){
            player.sendMessage(messages.getString("Commands.Back.NotFoundLocation"));
            return;
        }

        final Location back = data.getLastLocation();
        PlayerBackEvent playerBackEvent = new PlayerBackEvent(player, back);
        Bukkit.getPluginManager().callEvent(playerBackEvent);
        if(playerBackEvent.isCancelled()) return;

        player.teleport(back);
        player.sendMessage(messages.getString("Commands.Back.Teleport"));
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

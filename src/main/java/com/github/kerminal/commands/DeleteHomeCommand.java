package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.DataController;
import com.github.kerminal.controllers.LangController;
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
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class DeleteHomeCommand {

    private Kerminal plugin;
    private ConfigUtil commands;
    private final String identifierCommand = "Delhome";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public DeleteHomeCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, @Optional String nameHome) {
        final CommandSender sender = context.getSender();
        final LangController messages = plugin.getLangController();
        final Player player = (Player) sender;
        final int args = context.argsCount();
        final DataController controller = plugin.getController();

        PlayerData data = controller.getDataPlayer(player.getUniqueId());
        if(args == 0){
            player.sendMessage(messages.getString("Commands.HomeSection.Delhome.Usage").replace("%command%", command));
            return;
        }

        if(args == 1) {
            if(!data.getHomes().containsKey(nameHome)){
                player.sendMessage(messages.getString("Commands.HomeSection.Delhome.NotFound").replace("%name%", nameHome));
                return;
            }

            data.getHomes().remove(nameHome);
            plugin.getRepository().deleteHome(player, nameHome);
            player.sendMessage(messages.getString("Commands.HomeSection.Delhome.Success").replace("%name%", nameHome));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
        }
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
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }
}

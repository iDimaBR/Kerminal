package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class SpeedCommand {

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Speed";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public SpeedCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context) {
        final LangController messages = plugin.getLangController();
        final Player player = (Player) context.getSender();
        final int args = context.argsCount();

        if(args == 1){
            final String arg1 = context.getArg(0);
            if(arg1.equalsIgnoreCase("clear")){
                player.setFlySpeed(0.1f);
                player.setWalkSpeed(0.2f);
                player.sendMessage(messages.getString("Commands.Speed.BackDefault"));
                return;
            }

            float speed;
            try {
                speed = Float.parseFloat(arg1);
            } catch (NumberFormatException e) {
                player.sendMessage(messages.getString("Commands.Speed.InvalidSpeed"));
                return;
            }

            if(speed > 1){
                player.sendMessage(messages.getString("Commands.Speed.ExceedSpeed"));
                return;
            }

            player.setWalkSpeed(speed);
            player.setFlySpeed(speed);
            player.sendMessage(messages.getString("Commands.Speed.Success").replace("%speed%", speed+""));
            return;
        }

        if(args > 1){
            final String arg1 = context.getArg(0);
            final String arg2 = context.getArg(1);
            final Player target = Bukkit.getPlayer(arg2);
            if(target == null){
                player.sendMessage(messages.getString("DefaultCallback.PlayerNotFound"));
                return;
            }

            if(arg1.equalsIgnoreCase("clear")){
                target.setFlySpeed(0.1f);
                target.setWalkSpeed(0.2f);
                player.sendMessage(messages.getString("Commands.Speed.BackDefaultOther").replace("%target%", target.getName()));
                return;
            }

            float speed;
            try {
                speed = Float.parseFloat(arg1);
            } catch (NumberFormatException e) {
                player.sendMessage(messages.getString("Commands.Speed.InvalidSpeed"));
                return;
            }

            if(speed > 1){
                player.sendMessage(messages.getString("Commands.Speed.ExceedSpeed"));
                return;
            }

            target.setWalkSpeed(speed);
            target.setFlySpeed(speed);
            player.sendMessage(messages.getString("Commands.Speed.SuccessOther"));
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
                    onCommand(context);
                    return false;
                }
        );
    }
}

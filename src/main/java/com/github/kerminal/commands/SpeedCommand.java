package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
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

    private Kerminal plugin;
    private ConfigUtil commands;

    public SpeedCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Speed.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Speed.command"))
                        .aliases(commands.getStringList("Speed.aliases").toArray(new String[0]))
                        .permission(commands.getString("Speed.permission"))
                        .async(commands.getBoolean("Speed.async"))
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) context.getSender();
        final int args = context.argsCount();

        if(args == 1){
            final String arg1 = context.getArg(0);
            if(arg1.equalsIgnoreCase("clear")){
                player.setFlySpeed(0.1f);
                player.setWalkSpeed(0.2f);
                player.sendMessage("§aVocê voltou para a velocidade padrão");
                return;
            }

            float speed;
            try {
                speed = Float.parseFloat(arg1);
            } catch (NumberFormatException e) {
                player.sendMessage("§cColoque uma velocidade válida!!");
                return;
            }

            if(speed > 1){
                player.sendMessage("§cA velocidade não pode passar de 1.");
                return;
            }

            player.setWalkSpeed(speed);
            player.setFlySpeed(speed);
            player.sendMessage("§aVelocidade alterada para: §f" + speed);
            return;
        }

        if(args > 1){
            final String arg1 = context.getArg(0);
            final String arg2 = context.getArg(1);
            final Player target = Bukkit.getPlayer(arg2);
            if(target == null){
                player.sendMessage("§cO jogador " + arg2 + " não foi encontrado.");
                return;
            }

            if(arg1.equalsIgnoreCase("clear")){
                target.setFlySpeed(0.1f);
                target.setWalkSpeed(0.2f);
                player.sendMessage("§aO jogador " + arg2 + " voltou para a velocidade padrão");
                return;
            }

            float speed;
            try {
                speed = Float.parseFloat(arg1);
            } catch (NumberFormatException e) {
                player.sendMessage("§cColoque uma velocidade válida!!");
                return;
            }

            if(speed > 1){
                player.sendMessage("§cA velocidade não pode passar de 1.");
                return;
            }

            target.setWalkSpeed(speed);
            target.setFlySpeed(speed);
            player.sendMessage("§aVelocidade de " + arg2 + " alterada para: §f" + speed);
        }
    }
}

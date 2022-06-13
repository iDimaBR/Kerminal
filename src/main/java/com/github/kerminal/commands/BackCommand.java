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
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class BackCommand {

    private Kerminal plugin;

    @Command(
            name = "back",
            aliases = {"voltar"},
            permission = "kerminal.back",
            target = CommandTarget.PLAYER
    )
    public void onHome(Context<CommandSender> context) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) sender;
        final DataController controller = plugin.getController();

        PlayerData data = controller.getDataPlayer(player.getUniqueId());
        if(data.getLastLocation() == null){
            player.sendMessage("§cNenhuma localização foi registrada.");
            return;
        }

        Location back = data.getLastLocation();
        player.teleport(back);
        player.sendMessage("§aTeleportado de volta para última localização.");
    }
}

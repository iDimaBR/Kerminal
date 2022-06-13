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
public class DeleteHomeCommand {

    private Kerminal plugin;

    @Command(
            name = "delhome",
            aliases = {"deletehome","deletarhome","deletarcasa"},
            permission = "kerminal.delhome",
            target = CommandTarget.PLAYER
    )
    public void onDeleteHome(
            Context<CommandSender> context,
            @Optional String nameHome
    ) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) sender;
        final int args = context.argsCount();
        final DataController controller = plugin.getController();

        PlayerData data = controller.getDataPlayer(player.getUniqueId());
        if(args == 0){
            player.sendMessage("§cUtilize /delhome <nome>");
            return;
        }

        if(args == 1) {
            if (controller.getDataPlayer(player.getUniqueId()) == null) {
                player.sendMessage("§cSuas informações não foram carregadas, entre novamente no servidor.");
                return;
            }

            if(!data.getHomes().containsKey(nameHome)){
                player.sendMessage("§cHome não encontrada!");
                return;
            }

            data.getHomes().remove(nameHome);
            player.sendMessage("§aHome '§f" + nameHome + "§a' deletada com sucesso!");
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
        }
    }
}

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
public class SethomeCommand {

    private Kerminal plugin;

    @Command(
            name = "sethome",
            aliases = {"setcasa"},
            permission = "kerminal.sethome",
            target = CommandTarget.PLAYER
    )
    public void onSetHome(
            Context<CommandSender> context,
            @Optional String nameHome
    ) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) sender;
        final int args = context.argsCount();
        final DataController controller = plugin.getController();

        PlayerData data = controller.getDataPlayer(player.getUniqueId());
        if(data == null){
            player.sendMessage("§cSuas informações não foram carregadas, entre novamente no servidor.");
            return;
        }

        if(args == 0){
            player.sendMessage("§cUtilize /sethome <nome>");
            if(data.getDefaultHome() == null)
                player.sendMessage("§cPara definir sua casa principal utilize §7/sethome padrao");
            return;
        }

        if(args == 1) {
            if (nameHome.equalsIgnoreCase("padrao")){
                Home home = new Home(nameHome, player.getLocation(), false);
                data.setDefaultHome(home);
                player.sendMessage("§aHome principal setada com sucesso!");
                player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
                return;
            }

            int maxHomes = plugin.getConfig().getInt("Homes.MaxHomes");

            if(data.getHomes().size() >= maxHomes){
                player.sendMessage("§cVocê atingiu o limite de " + maxHomes + " casas.");
                return;
            }

            Home home = new Home(nameHome, player.getLocation(), false);
            data.getHomes().put(nameHome, home);
            player.sendMessage("§aHome setada com sucesso!");
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
        }
    }
}

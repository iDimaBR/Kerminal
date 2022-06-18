package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.DataController;
import com.github.kerminal.models.Home;
import com.github.kerminal.models.PlayerData;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.PermissionUtil;
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
public class SethomeCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public SethomeCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Sethome.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Sethome.command"))
                        .aliases(commands.getStringList("Sethome.aliases").toArray(new String[0]))
                        .permission(commands.getString("Sethome.permission"))
                        .async(commands.getBoolean("Sethome.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context, @Optional String nameHome) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) sender;
        final int args = context.argsCount();
        final DataController controller = plugin.getController();
        nameHome = nameHome.toLowerCase();

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
                Home home = new Home(nameHome, player.getLocation(), true);
                data.setDefaultHome(home);
                plugin.getStorage().saveHome(player, home);
                player.sendMessage("§aHome principal setada com sucesso!");
                player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
                return;
            }

            int maxHomes = PermissionUtil.getNumberPermission(player, commands.getString("Sethome.permission"));
            if(data.getHomes().size() >= maxHomes){
                player.sendMessage("§cVocê atingiu o limite de " + maxHomes + " casas.");
                return;
            }

            Home home = new Home(nameHome, player.getLocation(), false);
            data.getHomes().put(nameHome, home);
            plugin.getStorage().saveHome(player, home);
            player.sendMessage("§aHome setada com sucesso!");
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
        }
    }
}

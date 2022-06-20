package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.DataController;
import com.github.kerminal.controllers.LangController;
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

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Sethome";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public SethomeCommand(Kerminal plugin) {
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
        nameHome = nameHome.toLowerCase();

        final PlayerData data = controller.getDataPlayer(player.getUniqueId());
        if(isInvalidWorld(player.getLocation())){
            player.sendMessage(messages.getString("Commands.HomeSection.Sethome.NotAllowedWorld"));
            return;
        }

        if(args == 0){
            player.sendMessage(messages.getString("Commands.HomeSection.Sethome.Usage").replace("%command%", command));
            if(data.getDefaultHome() == null)
                player.sendMessage(messages.getString("Commands.HomeSection.DefaultHomeWarning"));
            return;
        }

        if(args == 1) {
            if (nameHome.equalsIgnoreCase(messages.getString("Commands.HomeSection.NameOfDefaultHome"))){
                Home home = new Home(nameHome, player.getLocation(), true);
                data.setDefaultHome(home);
                plugin.getStorage().saveHome(player, home);
                player.sendMessage(messages.getString("Commands.HomeSection.Sethome.DefineDefaultHome"));
                player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
                return;
            }

            int maxHomes = PermissionUtil.getNumberPermission(player, permission);
            if(data.getHomes().size() >= maxHomes){
                player.sendMessage(messages.getString("Commands.HomeSection.Sethome.ExceedLimit").replace("%limit%", maxHomes+""));
                return;
            }

            final Home home = new Home(nameHome, player.getLocation(), false);
            data.getHomes().put(nameHome, home);
            plugin.getStorage().saveHome(player, home);
            player.sendMessage(messages.getString("Commands.HomeSection.Sethome.DefineHome").replace("%home%", home.getName()));
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

    private boolean isInvalidWorld(Location location) {
        return plugin.getConfig().getStringList("BlockedWorlds.HomeCommand").contains(location.getWorld().getName());
    }
}

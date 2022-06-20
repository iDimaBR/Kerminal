package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.customevents.GamemodeChangeEvent;
import com.github.kerminal.customevents.PlayerHomeTeleportEvent;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Mode;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class GamemodeCommand {

    private Kerminal plugin;
    private ConfigUtil commands;
    private final String identifierCommand = "Gamemode";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public GamemodeCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();

        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context, @Optional String gamemode, @Optional String target) {
        final CommandSender sender = context.getSender();
        final LangController messages = plugin.getLangController();
        final Mode mode = Mode.of(gamemode);

        if (gamemode == null) {
            context.sendMessage(messages.getString("Gamemode.Usage").replace("%command%", command));
            return;
        }
        if (mode == null) {
            context.sendMessage(messages.getString("Gamemode.Usage").replace("%command%", command));
            return;
        }

        final String gamemodeName = mode.name();

        GameMode gameMode = mode.getGameMode();
        if (target != null) {
            final Player targetPlayer = Bukkit.getPlayer(target);

            if (targetPlayer == null) {
                context.sendMessage(messages.getString("DefaultCallback.PlayerNotFound"));
                return;
            }

            GamemodeChangeEvent gamemodeChangeEvent = new GamemodeChangeEvent(targetPlayer, gameMode);
            Bukkit.getPluginManager().callEvent(gamemodeChangeEvent);
            if(gamemodeChangeEvent.isCancelled()) return;

            targetPlayer.setGameMode(gameMode);
            context.sendMessage(messages.getString("Commands.Gamemode.SuccessOther").replace("%target%", targetPlayer.getName()).replace("%gamemode%", gamemodeName));
            targetPlayer.sendMessage(messages.getString("Commands.Gamemode.Success").replace("%gamemode%", gamemodeName));
            return;
        }

        if (sender instanceof Player) {
            GamemodeChangeEvent gamemodeChangeEvent = new GamemodeChangeEvent((Player) sender, gameMode);
            Bukkit.getPluginManager().callEvent(gamemodeChangeEvent);
            if(gamemodeChangeEvent.isCancelled()) return;

            ((Player) sender).setGameMode(gameMode);
            ((Player) sender).sendMessage(messages.getString("Commands.Gamemode.Success").replace("%gamemode%", gamemodeName));
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
                    onCommand(context, context.getArg(0), context.getArg(1));
                    return false;
                }
        );
    }
}

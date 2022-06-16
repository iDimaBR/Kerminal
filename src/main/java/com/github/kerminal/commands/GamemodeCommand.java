package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
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

    public GamemodeCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Gamemode.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Gamemode.command"))
                        .aliases(commands.getStringList("Gamemode.aliases").toArray(new String[0]))
                        .permission(commands.getString("Gamemode.permission"))
                        .async(commands.getBoolean("Gamemode.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0), context.getArg(1));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context, @Optional String gamemode, @Optional String target) {
        final CommandSender sender = context.getSender();
        final ConfigUtil messages = plugin.getMessages();
        final Mode mode = Mode.of(gamemode);

        if (gamemode == null) {
            context.sendMessage(messages.getString("Gamemode.Usage").replace("&", "§"));
            return;
        }
        if (mode == null) {
            context.sendMessage(messages.getString("Gamemode.Usage").replace("&", "§"));
            return;
        }

        final String gamemodeName = mode.name();

        //IF SENDER IS CONSOLE, REQUIRE TARGET
        GameMode gameMode = mode.getGameMode();
        if (target != null) {
            final Player targetPlayer = Bukkit.getPlayer(target);

            if (targetPlayer == null) {
                context.sendMessage("§cJogador não encontrado");
                return;
            }

            GamemodeChangeEvent gamemodeChangeEvent = new GamemodeChangeEvent(targetPlayer, gameMode);
            Bukkit.getPluginManager().callEvent(gamemodeChangeEvent);
            if(gamemodeChangeEvent.isCancelled()) return;

            targetPlayer.setGameMode(gameMode);
            context.sendMessage("§aModo do jogador §f" + targetPlayer.getName() + "§a alterado para §f" + gamemodeName);
            targetPlayer.sendMessage("§aSeu modo foi alterado por §f" + sender.getName() + "§a para §f" + gamemodeName);
            return;
        }

        // IF SENDER IS PLAYER, DO NOT REQUIRE TARGET
        if (sender instanceof Player) {
            GamemodeChangeEvent gamemodeChangeEvent = new GamemodeChangeEvent((Player) sender, gameMode);
            Bukkit.getPluginManager().callEvent(gamemodeChangeEvent);
            if(gamemodeChangeEvent.isCancelled()) return;

            ((Player) sender).setGameMode(gameMode);
            ((Player) sender).sendMessage("§aSeu modo de jogo foi alterado para:§f " + gamemodeName);
        }
    }
}

package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.Mode;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class GamemodeCommand {

    private Kerminal plugin;
    @Command(
            name = "gamemode",
            aliases = {"gm"},
            description = "Muda seu gamemode :P",
            permission = "kerminal.gamemode"
    )
    public void onGamemode(
            Context<CommandSender> context,
            @Optional String gamemode,
            @Optional String target
    ) {
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
        //IF SENDER IS CONSOLE, REQUIRE TARGET
        if (target != null) {
            final Player targetPlayer = Bukkit.getPlayer(target);

            if (targetPlayer == null) {
                context.sendMessage("§cJogador não encontrado");
                return;
            }

            targetPlayer.setGameMode(mode.getGameMode());
            context.sendMessage("§aModo do jogador §f" + targetPlayer.getName() + "§a alterado para §f" + mode.getGameMode());
            targetPlayer.sendMessage("§aSeu modo foi alterado por §f" + sender.getName() + "§a para §f" + mode.getGameMode());
            return;

        }
        // IF SENDER IS PLAYER, DO NOT REQUIRE TARGET
        if (sender instanceof Player) {
            ((Player) sender).setGameMode(mode.getGameMode());
            ((Player) sender).sendMessage("§aSeu modo de jogo foi alterado para:§f " + mode.getGameMode());
        }
    }
}

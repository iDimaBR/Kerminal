package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.HelpGui;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class HelpCommand {

    private Kerminal plugin;

    @Command(
            name = "ajuda",
            aliases = {"help.kerminal", "ajuda.kerminal"},
            description = "Mostra tudo :P"
    )
    public void Help(Context<CommandSender> c) {
        Player p = (Player) c.getSender();

        if (p instanceof Player) {

            p.openInventory(HelpGui.getHelpInventory());
        }
    }
}

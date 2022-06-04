package com.github.idimabr.commands;


import com.github.idimabr.Kerminal;
import com.github.idimabr.utils.HelpGui;
import com.github.idimabr.utils.Mode;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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

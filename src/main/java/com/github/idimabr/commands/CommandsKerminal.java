package com.github.idimabr.commands;


import com.github.idimabr.Kerminal;
import com.github.idimabr.utils.Inventorys;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CommandsKerminal {

    private Kerminal plugin;

    @Command(
            name = "ajuda",
            description = "Mostra tudo :P"
    )
    public void Ajuda(Context<CommandSender> c) {
        Player p = (Player) c.getSender();
        p.openInventory(Inventorys.getHelpInventory());
    }

}

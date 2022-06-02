package com.github.idimabr.commands;


import com.github.idimabr.Kerminal;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class NBTCommand {

    private Kerminal plugin;

    @Command(
            name = "nbtedit",
            description = "Altera os campos de NBT de um determinado item"
    )
    public void onNBTCommand(Context<CommandSender> c) {
        // to do
    }

}

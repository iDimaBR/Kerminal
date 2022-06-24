package com.github.kerminal.commands;

import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.ItemBuilder;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class HeadCommands {
    private Kerminal plugin;
    private ConfigUtil commands;
    private final String identifierCommand = "Head";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public HeadCommands(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }
    public void onCommand(
            Context<CommandSender> context,
            @Optional String target
    ) {
        final CommandSender sender = context.getSender();
        final LangController messages = plugin.getLangController();
        final Player player = (Player) sender;

        if (target == null) {
            context.sendMessage(messages.getString("Commands.Head.Usage").replace("%command%", command));
            return;
        }
        if (target != null) {
            ItemBuilder skull = new ItemBuilder(Material.SKULL_ITEM);
            skull.setDurability((short) 3);
            skull.setLore("§eVocê está com a cabeça de §f" + target + " §ena mão safadinho.");
            skull.setName("§eCabeça de §f" + target);
            skull.setSkullOwner(target);

            player.getInventory().addItem(skull.build());
            player.sendMessage(messages.getString("Commands.Head.Success").replace("%target%", target));
        }




    }
    public void register(){
        if (!commands.getBoolean(identifierCommand + ".enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(command)
                        .aliases(aliases)
                        .permission(permission)
                        .target(CommandTarget.PLAYER)
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }
}

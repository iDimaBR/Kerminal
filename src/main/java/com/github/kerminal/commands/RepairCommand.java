package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.RepairUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class RepairCommand {

    private final Kerminal plugin;
    private final ConfigUtil commands;
    private final String identifierCommand = "Repair";
    private final String command;
    private final String[] aliases;
    private final String permission;

    public RepairCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        this.command = commands.getString(identifierCommand + ".command");
        this.aliases = commands.getStringList(identifierCommand + ".aliases").toArray(new String[0]);
        this.permission = commands.getString(identifierCommand + ".permission");
    }

    public void onCommand(Context<CommandSender> context) {
        final LangController messages = plugin.getLangController();
        final Player player = (Player) context.getSender();
        final int args = context.argsCount();

        if(args == 0){
            final ItemStack item = player.getItemInHand();
            if(item == null || item.getType().getMaxDurability() == 0){
                player.sendMessage(messages.getString("DefaultCallback.HandEmpty"));
                return;
            }

            if (item.getDurability() == 0) {
                player.sendMessage(messages.getString("Commands.Repair.IsFull"));
                return;
            }

            RepairUtil.repair(item);
            player.sendMessage(messages.getString("Commands.Repair.Success"));
            return;
        }

        if(args > 0 && context.getArg(0).equalsIgnoreCase("all")){
            if(!player.hasPermission(permission + ".all")){
                player.sendMessage(messages.getString("DefaultCallback.NoPermission"));
                return;
            }

            RepairUtil.repairAll(player.getInventory());
            player.sendMessage(messages.getString("Commands.Repair.SuccessAll"));
            return;
        }

        player.sendMessage(messages.getString("Commands.Repair.Usage").replace("%command%", command));
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
                    onCommand(context);
                    return false;
                }
        );
    }
}

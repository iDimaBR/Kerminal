package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class RepairCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public RepairCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Slime.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Repair.command"))
                        .aliases(commands.getStringList("Repair.aliases").toArray(new String[0]))
                        .permission(commands.getString("Repair.permission"))
                        .async(commands.getBoolean("Repair.async"))
                        .build(),
                context -> {
                    onCommand(context);
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context) {
        final ConfigUtil messages = plugin.getMessages();
        final Player player = (Player) context.getSender();
        final int args = context.argsCount();

        if(args == 0){
            final ItemStack item = player.getItemInHand();
            if(item == null || item.getType().getMaxDurability() == 0){
                player.sendMessage("§cSegure um item válido em sua mão para reparar.");
                return;
            }

            if (item.getDurability() == 0) {
                player.sendMessage("§cEsse item não precisa ser reparado");
                return;
            }

            item.setDurability((short) 0);
            player.sendMessage("§aItem reparado!");
            return;
        }

        if(args > 0 && context.getArg(0).equalsIgnoreCase("all")){
            if(!player.hasPermission(commands.getString("Repair.permission") + ".all")){
                player.sendMessage("§cSem permissão!");
                return;
            }

            final ItemStack[] contents = player.getInventory().getContents();
            for (ItemStack item : contents) {
                if(item == null) continue;
                if(item.getType().getMaxDurability() == 0) continue;
                if(item.getDurability() == 0) continue;

                item.setDurability((short) 0);
            }

            player.sendMessage("§aTodos seus itens foram reparados!");
            return;
        }

        player.sendMessage("§cUtilize /reparar <all>");
    }
}

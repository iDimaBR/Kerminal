package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.RepairUtil;
import lombok.AllArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
public class RepairSignListener implements Listener {
    private Kerminal plugin;

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        final ConfigUtil config = plugin.getConfig();
        final LangController messages = plugin.getLangController();
        final Player player = e.getPlayer();
        final String[] lines = e.getLines();
        if(!lines[0].equalsIgnoreCase("[reparar]")) return;
        if(!player.hasPermission("kerminal.sign.repair.create")) {
            e.setCancelled(true);
            e.getBlock().breakNaturally();
            player.sendMessage(messages.getString("DefaultCallback.NoPermission"));
            return;
        }

        final List<String> signModel = config.getStringList("SignRepair.SignModel");
        for(int i = 0;i < Math.max(signModel.size(), 4);i++){
            e.setLine(i, signModel.get(i));
        }
        player.sendMessage(messages.getString("RepairSign.CreatedSign"));
    }

    @EventHandler
    public void onInteractSign(PlayerInteractEvent e) {
        final ConfigUtil config = plugin.getConfig();
        final LangController messages = plugin.getLangController();
        final Player player = e.getPlayer();
        final Block block = e.getClickedBlock();
        final String action = e.getAction().name();
        if(!action.contains("BLOCK")) return;
        if(block == null) return;
        if(!block.getType().name().contains("SIGN")) return;

        final Sign sign = (Sign) block.getState();
        if(!isValidSign(sign.getLines(), config.getStringList("SignRepair.SignModel"))) return;

        if(action.contains("LEFT") && player.hasPermission("kerminal.sign.repair")){
            RepairUtil.repair(player.getItemInHand());
            player.sendMessage(messages.getString("RepairSign.Success"));
        }else if(action.contains("RIGHT") && player.hasPermission("kerminal.sign.repairall")){
            RepairUtil.repairAll(player.getInventory());
            player.sendMessage(messages.getString("RepairSign.SuccessFull"));
        }else{
            player.sendMessage(messages.getString("DefaultCallback.NoPermission"));
        }
    }

    private boolean isValidSign(String[] lines, List<String> match){
        return match.equals(Arrays.stream(lines).collect(Collectors.toList()));
    }
}

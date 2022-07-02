package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.controllers.LangController;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.RepairUtils;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;


@AllArgsConstructor
public class SignListener implements Listener {
    private Kerminal plugin;

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        LangController messages = plugin.getLangController();
        Player player = e.getPlayer();
        if (messages.getBoolean("SignRepair.enabled")) {
            if (e.getLine(0).equalsIgnoreCase("kerminal repair")
                    || e.getLine(0).equalsIgnoreCase(messages.getString("SignRepair.title"))
            ) {
                if (player.hasPermission(messages.getString("SignRepair.permission"))) {
                    e.setLine(0, messages.getString("SignRepair.title"));
                    e.setLine(1, messages.getString("SignRepair.subtitle"));
                } else {
                    e.setCancelled(true);
                    e.getBlock().breakNaturally();
                }
            }

            if (e.getLine(0).equalsIgnoreCase("kerminal repairall") || e.getLine(0).equalsIgnoreCase(messages.getString("SignRepair.titleall"))) {
                if (player.hasPermission(messages.getString("SignRepair.permissionall"))) {
                    e.setLine(0, messages.getString("SignRepair.titleall"));
                    e.setLine(1, messages.getString("SignRepair.subtitleall"));
                } else {
                    e.setCancelled(true);
                    e.getBlock().breakNaturally();
                }

            }
        }
    }



    @EventHandler
    public void onInteractSign(PlayerInteractEvent e) {
        LangController messages = plugin.getLangController();
        Player p = e.getPlayer();
        Block block = e.getClickedBlock();
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            if(block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) block.getState();
                if (p.hasPermission("kerminal.signrepair")) {
                    if (sign.getLine(0).equalsIgnoreCase(messages.getString("SignRepair.title"))) {
                        if (sign.getLine(1).equalsIgnoreCase(messages.getString("SignRepair.subtitle"))) {
                            RepairUtils.repairHand(p);
                            p.sendMessage("§aReparando...");
                            p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);
                            p.sendMessage("§aReparado com sucesso!");
                        }
                    }
                    if (sign.getLine(0).equalsIgnoreCase(messages.getString("SignRepair.titleall"))) {
                        if (sign.getLine(1).equalsIgnoreCase(messages.getString("SignRepair.subtitleall"))) {
                            p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);
                            p.sendMessage("§aReparando...");
                            p.performCommand("repair all");
                        }
                    }
                }
            }
        }
    }
}

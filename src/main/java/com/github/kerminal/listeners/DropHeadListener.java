package com.github.kerminal.listeners;

import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DropHeadListener implements Listener {

    private Kerminal plugin;

    @EventHandler
    public void onSkullDeath(PlayerDeathEvent e) {
        final Player player = e.getEntity();
        final ConfigUtil config = plugin.getConfig();

        final int chance = config.getInt("Features.Heads.Chance");
        if(RandomUtils.nextInt(100) > chance) return;

        final String itemName = config.getString("Features.Heads.Name");
        final List<String> lore = config.getStringList("Features.Heads.Lore");
        final ItemStack skull = getSkull(player.getName(), itemName, lore);

        player.getWorld().dropItemNaturally(player.getLocation(), skull);
    }

    private ItemStack getSkull(String name, String displayname, List<String> lore){
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if(!displayname.isEmpty())
            skullMeta.setDisplayName(displayname.replace("%player%", name));
        if(!lore.isEmpty())
            skullMeta.setLore(
                    lore.stream().map(
                            $ -> $.replace("%player%", name)
                    ).collect(Collectors.toList())
            );
        skullMeta.setOwner(name);
        skull.setItemMeta(skullMeta);
        return skull;
    }
}

package com.github.idimabr;

import com.github.idimabr.commands.CommandsKerminal;
import com.github.idimabr.listeners.GameMechanicsListener;
import com.github.idimabr.listeners.InventoryListener;
import com.github.idimabr.utils.Inventorys;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Kerminal extends JavaPlugin {

    private BukkitFrame bukkitFrame;

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage("Ta piscando ta gravando");

        registerListeners();
        registerCommands();
    }

    public void onLoad() {

        saveDefaultConfig();
    }

    private void registerCommands() {
        bukkitFrame = new BukkitFrame(this);

        bukkitFrame.registerCommands(
                new CommandsKerminal(this)
        );
    }
    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(
                new InventoryListener(this), this
        );
        pluginManager.registerEvents(
                new GameMechanicsListener(this), this
        );
    }

    @Override
    public void onDisable() {
    }
}

package com.github.idimabr;

import com.github.idimabr.commands.CommandsKerminal;
import com.github.idimabr.listeners.GameMechanicsListener;
import com.github.idimabr.listeners.InventoryListener;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

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
        MessageHolder messageHolder = bukkitFrame.getMessageHolder();
        messageHolder.setMessage(
                MessageType.NO_PERMISSION, "§cVocê não tem permissão para executar este comando!"
        );
        messageHolder.setMessage(
                MessageType.INCORRECT_TARGET, "§cVocê não pode executar este comando "
        );
        messageHolder.setMessage(
                MessageType.ERROR, "§cOcorreu um erro ao executar este comando!"
        );

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

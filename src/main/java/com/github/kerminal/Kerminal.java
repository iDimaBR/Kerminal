package com.github.kerminal;

import com.github.kerminal.commands.*;
import com.github.kerminal.controllers.DataController;
import com.github.kerminal.listeners.*;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.storage.MySQL;
import com.github.kerminal.tasks.RegenerationTask;
import com.github.kerminal.tasks.TeleportTask;
import com.github.kerminal.utils.ConfigUtil;
import com.google.common.collect.Maps;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.bukkit.command.command.BukkitCommand;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.apache.commons.lang3.ClassUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public final class Kerminal extends JavaPlugin {

    private BukkitFrame bukkitFrame;
    private DataController controller;
    private MySQL SQL;
    private ConfigUtil config;
    private ConfigUtil configurableCommands;
    private ConfigUtil messages;
    private ConfigUtil commands;


    public void onLoad() {
        saveDefaultConfig();
        loadConfigs();
    }

    @Override
    public void onEnable() {
        loadStorage();
        loadStorageData();
        loadController();
        registerListeners();
        registerCommands();
        loadTicksWorld();
        loadRegenSystem();
    }

    @Override
    public void onDisable() {
        saveAllPlayers();
    }

    private void loadConfigs(){
        config = new ConfigUtil(this, "config");
        configurableCommands = new ConfigUtil(this,"configurableCommands");
        messages = new ConfigUtil(this,"messages");
        commands = new ConfigUtil(this,"commands");

    }

    private void registerCommands() {
        bukkitFrame = new BukkitFrame(this);
        MessageHolder messageHolder = bukkitFrame.getMessageHolder();
        messageHolder.setMessage(
                MessageType.NO_PERMISSION, "§cVocê não tem permissão para executar este comando!"
        );
        messageHolder.setMessage(
                MessageType.INCORRECT_USAGE, "§cUtilize o comando /{usage}"
        );
        messageHolder.setMessage(
                MessageType.INCORRECT_TARGET, "§cVocê não pode executar este comando!"
        );
        messageHolder.setMessage(
                MessageType.ERROR, "§cOcorreu um erro ao executar este comando!"
        );

        final Map<Object, Boolean> commandsToEnable = Maps.newHashMap();
        commandsToEnable.put(new TpaCommand(this), hasRegistryCommand("tpa"));
        commandsToEnable.put(new GamemodeCommand(this), hasRegistryCommand("gamemode"));
        commandsToEnable.put(new TeleportCommand(this), hasRegistryCommand("tp"));
        commandsToEnable.put(new FlyCommand(this), hasRegistryCommand("fly"));
        commandsToEnable.put(new EnchantsCommands(this), hasRegistryCommand("enchant"));
        commandsToEnable.put(new ClearChatCommand(this), hasRegistryCommand("clearchat"));
        commandsToEnable.put(new CraftCommand(this), hasRegistryCommand("craft"));
        commandsToEnable.put(new FeedCommand(this), hasRegistryCommand("feed"));
        commandsToEnable.put(new HealCommand(this), hasRegistryCommand("heal"));
        commandsToEnable.put(new HatCommand(this), hasRegistryCommand("hat"));
        commandsToEnable.put(new ClearCommand(this), hasRegistryCommand("clear"));
        commandsToEnable.put(new EnderChestCommand(this), hasRegistryCommand("enderchest"));
        commandsToEnable.put(new AnnounceCommand(this), hasRegistryCommand("divulgar"));
        commandsToEnable.put(new LightCommand(this), hasRegistryCommand("luz"));
        commandsToEnable.put(new TrashCommand(this), hasRegistryCommand("lixo"));
        commandsToEnable.put(new OnlineCommand(this), hasRegistryCommand("onlines"));
        commandsToEnable.put(new OnlinePlayersCommand(this), hasRegistryCommand("jogadores"));
        commandsToEnable.put(new HomeCommand(this), hasRegistryCommand("home"));
        commandsToEnable.put(new SethomeCommand(this), hasRegistryCommand("sethome"));
        commandsToEnable.put(new ListHomesCommand(this), hasRegistryCommand("listhomes"));
        commandsToEnable.put(new DeleteHomeCommand(this), hasRegistryCommand("delhome"));
        commandsToEnable.put(new BackCommand(this), hasRegistryCommand("back"));
        commandsToEnable.put(new PingCommand(this), hasRegistryCommand("ping"));

        final List<Object> commandsEnableds = commandsToEnable.entrySet().stream()
                .filter($ -> $.getValue().equals(true))
                .map(Map.Entry::getKey).collect(Collectors.toList());

        for (Object command : commandsEnableds)
            bukkitFrame.registerCommands(command);
    }

    private boolean hasRegistryCommand(String name){
        if(!commands.isSet(name)) return true;
        return commands.getBoolean(name);
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(
                new GameMechanicsListener(this), this
        );
        pluginManager.registerEvents(
                new ConfigurableCommandsHandler(this), this
        );
        pluginManager.registerEvents(
                new EnderChestListener(), this
        );
        pluginManager.registerEvents(
                new CacheListener(this), this
        );
        pluginManager.registerEvents(
                new BackListener(this), this
        );
    }

    private void loadRegenSystem() {
        if(config.getBoolean("Regeneration.Enabled")){
            final long delay = config.getLong("Regeneration.Delay");
            new RegenerationTask().runTaskTimerAsynchronously(this, delay, delay);
            PluginManager pluginManager = getServer().getPluginManager();
            pluginManager.registerEvents(
                    new RegenerationListener(), this
            );
            getLogger().info("Regeneration System: ENABLED");
        }else{
            getLogger().info("Regeneration System: DISABLED");
        }
    }

    private void loadTicksWorld(){
        for (World world : Bukkit.getWorlds()) {
            if(!config.isSet("Worlds." + world.getName())) continue;

            final int ticks = config.getInt("Worlds." + world.getName() + ".Tick");
            world.setGameRuleValue("randomTickSpeed", String.valueOf(ticks));
        }
    }

    private void loadController(){
        controller = new DataController();
        new TeleportTask(new TeleportRegistry());
    }

    private void loadStorage(){
        SQL = new MySQL(this);
        SQL.createTable();
    }

    private void loadStorageData(){
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        if(onlinePlayers.size() == 0) return;
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                int loaded = 0;
                for (Player player : onlinePlayers) {
                    if(!SQL.loadPlayer(player.getUniqueId())) {
                        player.kickPlayer("§cSuas informações não foram carregadas, entre em contato com um administrador.");
                        continue;
                    }

                    loaded++;
                }
                Bukkit.getLogger().info(loaded + " jogadores foram carregados de um total de " + onlinePlayers.size());
            }
        });
    }

    private void saveAllPlayers(){
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        if(onlinePlayers.size() == 0) return;

        for (Player player : onlinePlayers)
            SQL.savePlayer(player.getUniqueId());
    }
}

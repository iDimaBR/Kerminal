package com.github.kerminal;

import com.github.kerminal.commands.*;
import com.github.kerminal.controllers.DataController;
import com.github.kerminal.controllers.EntityController;
import com.github.kerminal.controllers.KitController;
import com.github.kerminal.listeners.*;
import com.github.kerminal.models.Warp;
import com.github.kerminal.registry.TeleportRegistry;
import com.github.kerminal.storage.MySQL;
import com.github.kerminal.tasks.AutoMessageTask;
import com.github.kerminal.tasks.RegenerationTask;
import com.github.kerminal.tasks.TeleportTask;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.LocationUtils;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

@Getter
public final class Kerminal extends JavaPlugin {

    private BukkitFrame bukkitFrame;
    private TeleportRegistry teleportRegistry;
    private DataController controller;
    private EntityController entityController;
    private KitController kitController;
    private MySQL Storage;
    private ConfigUtil config;
    private ConfigUtil configurableCommands;
    private ConfigUtil messages;
    private ConfigUtil commands;
    private ConfigUtil entities;
    private ConfigUtil kits;
    private ConfigUtil locations;

    @Setter
    private Location Spawn;
    private final Map<String, Warp> warpsList = Maps.newHashMap();

    public void onLoad() {
        saveDefaultConfig();
        loadConfigs();
    }

    @Override
    public void onEnable() {
        loadLocations();
        loadControllers();
        loadStorage();
        registerListeners();
        registerCommands();
        loadTicksWorld();
        loadRegenSystem();
        loadAutoMessageSystem();
    }

    @Override
    public void onDisable() {
        //saveAllPlayers();
    }

    private void loadConfigs(){
        config = new ConfigUtil(this, "config");
        configurableCommands = new ConfigUtil(this,"customCommands");
        messages = new ConfigUtil(this,"messages");
        commands = new ConfigUtil(this,"commands");
        entities = new ConfigUtil(this, "entities");
        kits = new ConfigUtil(this, "kits", "data");
        locations = new ConfigUtil(this, "locations", "data");
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

        new SlimeCommand(this);
        new PingCommand(this);
        new BackCommand(this);
        new AnnounceCommand(this);
        new ClearChatCommand(this);
        new ClearCommand(this);
        new CraftCommand(this);
        new DeleteHomeCommand(this);
        new EnchantsCommand(this);
        new EnderChestCommand(this);
        new FeedCommand(this);
        new FlyCommand(this);
        new GamemodeCommand(this);
        new HatCommand(this);
        new HealCommand(this);
        new HomeCommand(this);
        new LightCommand(this);
        new ListHomesCommand(this);
        new OnlineCommand(this);
        new OnlinePlayersCommand(this);
        new SethomeCommand(this);
        new TeleportCommand(this);
        new TpaCommand(this);
        new TrashCommand(this);
        new SpawnCommand(this);
        new SetspawnCommand(this);
        new InfolagCommand(this);
        new CreateKitCommand(this);
        new KitCommand(this);
        new ListKitsCommand(this);
        new NickCommand(this);
        new ColorsCommand(this);
        new WarpCommand(this);
        new SetWarpCommand(this);
        new WarpListCommand(this);
        new RepairCommand(this);
        new SpeedCommand(this);
        new InvseeCommand(this);
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(
                new MechanicListener(this), this
        );
        if(config.getBoolean("Features.CustomMessages.Enabled", true))
            pluginManager.registerEvents(
                    new PlayerActionsChangeListener(this), this
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
        if(commands.getBoolean("Back.enabled", true))
            pluginManager.registerEvents(
                    new BackListener(this), this
            );
        pluginManager.registerEvents(
                new EntityDataListener(this), this
        );
        if(!config.getStringList("BlockedCommands").isEmpty())
            pluginManager.registerEvents(
                    new BlockCommandListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Decay-Leaves", false))
            pluginManager.registerEvents(
                    new BlockDecayListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Explode-Item", false))
            pluginManager.registerEvents(
                    new BlockExplodeItemListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Entity-Burn", false))
            pluginManager.registerEvents(
                    new BlockFireEntityListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Fire-Spread", false))
            pluginManager.registerEvents(
                    new BlockFireListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Food-Down", false))
            pluginManager.registerEvents(
                    new BlockFoodWorldListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Freeze-Water", false))
            pluginManager.registerEvents(
                    new BlockFreezeListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Entity-Steal-Item", false))
            pluginManager.registerEvents(
                    new BlockItemEntityListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Plantation-Destroy", false))
            pluginManager.registerEvents(
                    new BlockPlantationDamageListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Create-Portal", false))
            pluginManager.registerEvents(
                    new BlockPortalListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Smelt-Snow", false))
            pluginManager.registerEvents(
                    new BlockSmeltSnowListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Void-Damage", false))
            pluginManager.registerEvents(
                    new BlockVoidListener(this), this
            );

        if(config.getBoolean("Features.Options.Block-Weather", false))
            pluginManager.registerEvents(
                    new BlockWeatherListener(this), this
            );

        pluginManager.registerEvents(
                new BlockCraftListener(this), this
        );
        pluginManager.registerEvents(
                new BlockDoubleLoginListener(this), this
        );
        if(config.getBoolean("Features.Options.Block-Vehicle-Enter", true))
            pluginManager.registerEvents(
                    new BlockVehicleListener(this), this
            );
        if(config.getBoolean("Features.Options.Block-Naturally-Spawn", false))
            pluginManager.registerEvents(
                    new BlockNaturalSpawnListener(this), this
            );
        if(config.getBoolean("Features.Heads.Enabled", false))
            pluginManager.registerEvents(
                    new DropHeadListener(this), this
            );
        pluginManager.registerEvents(
                new TitleJoinListener(this), this
        );
        pluginManager.registerEvents(
                new InvseeListener(), this
        );
        pluginManager.registerEvents(
                new AnvilColorListener(this), this
        );
    }

    private void loadRegenSystem() {
        boolean enabled = config.getBoolean("Regeneration.Enabled");
        getLogger().info("Sistema de Regeneração: " + (enabled ? "Ativado" : "Desativado"));
        if(enabled){
            long delay = config.getLong("Regeneration.Delay");
            if(delay < 5){
                delay = 5;
                getLogger().warning("O intervalo da regeneração não pode ser abaixo de 5 ticks.");
            }

            new RegenerationTask().runTaskTimerAsynchronously(this, delay, delay);
            PluginManager pluginManager = getServer().getPluginManager();
            pluginManager.registerEvents(
                    new RegenerationListener(), this
            );
        }
    }

    private void loadTicksWorld(){
        for (World world : Bukkit.getWorlds()) {
            if(!config.isSet("Worlds." + world.getName())) continue;

            final int ticks = config.getInt("Worlds." + world.getName() + ".Tick");
            world.setGameRuleValue("randomTickSpeed", String.valueOf(ticks));
        }
    }

    private void loadControllers(){
        teleportRegistry = new TeleportRegistry(this);
        controller = new DataController(this, teleportRegistry);
        entityController = new EntityController(this, entities);
        kitController = new KitController(this, kits);

        entityController.loadEntityData();
        kitController.loadAllKits();

        new TeleportTask(teleportRegistry);
    }

    private void loadLocations(){
        Spawn = LocationUtils.getLocationFromConfig(locations, "Spawn");

        if(locations.isSet("Warps"))
            for (String warpKey : locations.getConfigurationSection("Warps").getKeys(false)) {
                final Location location = LocationUtils.getLocationFromConfig(locations, "Warps." + warpKey);
                warpsList.put(warpKey, new Warp(warpKey, location));
            }
    }

    private void loadStorage(){
        Storage = new MySQL(this);
        Storage.createTableHomes();
        Storage.createTableKits();
    }

    private void loadAutoMessageSystem(){
        boolean enabled = config.getBoolean("Features.AutoMessage.Enabled");
        getLogger().info("Sistema de Auto-Mensagem: " + (enabled ? "Ativado" : "Desativado"));
        if(!enabled) return;

        final int delayMessage = config.getInt("Features.AutoMessage.Delay");
        new AutoMessageTask(this).runTaskTimerAsynchronously(this, 80L, delayMessage * 20L);
    }
}

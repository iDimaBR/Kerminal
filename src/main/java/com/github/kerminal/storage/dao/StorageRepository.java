package com.github.kerminal.storage.dao;

import java.util.*;

import com.github.kerminal.Kerminal;
import com.github.kerminal.models.Home;
import com.github.kerminal.models.Kit;
import com.github.kerminal.models.PlayerData;
import com.github.kerminal.storage.adapter.HomeAdapter;
import com.github.kerminal.utils.LocationAdapter;
import com.google.common.collect.Maps;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class StorageRepository {

    private Kerminal plugin;

    public StorageRepository(Kerminal plugin) {
        this.plugin = plugin;
    }

    public void createTableHomes() {
        this.executor().updateQuery("CREATE TABLE IF NOT EXISTS homes(" +
                "`uuid` varchar(36) NOT NULL, " +
                "`name` varchar(50) PRIMARY KEY NOT NULL, " +
                "`is_default` bit NOT NULL DEFAULT 0, " +
                "`location` varchar(80) NOT NULL" +
                ")");
    }

    public void createTableKits() {
        this.executor().updateQuery("CREATE TABLE IF NOT EXISTS kits(" +
                "`uuid` varchar(36) NOT NULL," +
                " `delay` bigint NOT NULL, " +
                "`kit_name` varchar(50) PRIMARY KEY NOT NULL" +
                ")");
    }

    public void insertDelayKit(Player player, String kit, long delay) {
        final UUID uuid = player.getUniqueId();
        this.executor().updateQuery("REPLACE INTO kits(uuid, delay, kit_name) VALUES(?,?,?)",
                statement -> {
                    statement.set(1, uuid.toString());
                    statement.set(2, delay);
                    statement.set(3, kit);
                });
    }

    public void saveHome(Player player, Home home) {
        final UUID uuid = player.getUniqueId();
        final Location location = home.getLocation();
        this.executor().updateQuery("REPLACE INTO homes(uuid, name, is_default, location) VALUES(?,?,?,?)",
                statement -> {
                    statement.set(1, uuid.toString());
                    statement.set(2, home.getName());
                    statement.set(3, home.isDefault());
                    statement.set(4, LocationAdapter.toString(location));
                });
    }

    public void deleteHome(Player player, String home) {
        final UUID uuid = player.getUniqueId();
        this.executor().updateQuery("DELETE FROM homes WHERE uuid = ? AND name = ?",
                statement -> {
                    statement.set(1, uuid.toString());
                    statement.set(2, home);
                });
    }

    public void loadData(UUID uuid) {
        loadKitsDelay(uuid);
        loadHomes(uuid);
    }

    public void loadKitsDelay(UUID uuid) {
        this.executor().resultQuery("SELECT * FROM kits WHERE UUID = ?;",
                statement -> {
                    statement.set(1, uuid.toString());
                }, resultSet -> {
                    while (resultSet.next()) {
                        final String kitName = resultSet.get("kit_name");
                        final long delay = resultSet.get("delay");

                        if (delay < System.currentTimeMillis()) continue;

                        final Kit kit = plugin.getKitController().getKit(kitName);
                        if (kit == null) continue;
                        final Map<UUID, Long> delayKit = kit.getCooldownMap();

                        delayKit.put(uuid, delay);
                    }
                    return null;
                });
    }

    public void loadHomes(UUID uuid) {
        final PlayerData data = plugin.getController().getDataPlayer(uuid);
        final HashMap<String, Home> homes = Maps.newHashMap();
        for (Home home : getAllHomes(uuid)) {
            if (home.isDefault()) {
                data.setDefaultHome(home);
                continue;
            }

            homes.put(home.getName(), home);
        }
        data.setHomes(homes);
        plugin.getController().getDATALIST().put(uuid, data);
    }

    public Set<Home> getAllHomes(UUID uuid) {
        return this.executor().resultManyQuery(
                "SELECT * FROM homes WHERE UUID = ?;",
                statement -> {
                    statement.set(1, uuid.toString());
                },
                HomeAdapter.class
        );
    }

    private SQLExecutor executor() {
        return new SQLExecutor(plugin.getStorage());
    }

}
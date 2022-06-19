package com.github.kerminal.storage;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.github.kerminal.Kerminal;
import com.github.kerminal.models.Home;
import com.github.kerminal.models.Kit;
import com.github.kerminal.models.PlayerData;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MySQL {

	private Kerminal plugin;
	private Connection connection;
	private PreparedStatement smt;
	private String connectQuery;
	private String host;
	private int port;
	private String database;
	private String username;
	private String password;

	public MySQL(Kerminal plugin) {
		this.plugin = plugin;
		final FileConfiguration config = plugin.getConfig();
		final String type = config.getString("ConnectionType").toLowerCase();
		try {
			database = config.getString("Connection.Database");
			switch(type){
				case "sqlite":
					Class.forName("org.sqlite.JDBC").newInstance();
					connectQuery = "jdbc:sqlite://" + plugin.getDataFolder().getAbsolutePath() + "/" + database + ".db";
					break;
				case "mysql":
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					host = config.getString("Connection.Host");
					port = config.getInt("Connection.Port");
					username = config.getString("Connection.Username");
					password = config.getString("Connection.Password");

					connectQuery = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&" + "user=" + username + "&password=" + password;
					break;
				default:
					break;
			}

			connection = DriverManager.getConnection(connectQuery);
			plugin.getLogger().info("§aConexão com banco de dados foi estabelecida. [" + type + "]");
		} catch (Exception e) {
			plugin.getLogger().info("§cOcorreu um erro no banco de dados: " + e.getLocalizedMessage());
			Bukkit.getPluginManager().disablePlugin(plugin);
		}
	}

	public void createTableHomes() {
		try {
			smt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS homes(`uuid` varchar(36) NOT NULL, `name` varchar(50) NOT NULL, `is_default` bit NOT NULL DEFAULT 0, `world` varchar(50) NOT NULL, `x` int NOT NULL, `y` int NOT NULL, `z` int NOT NULL, `pitch` double NOT NULL, `yaw` double, PRIMARY KEY (name))");
			smt.executeUpdate();
		} catch (SQLException e) {
			plugin.getLogger().info("Erro na criação da tabela no MYSQL: " + e.getLocalizedMessage());
		}
	}

	public void createTableKits() {
		try {
			smt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS kits(`uuid` varchar(36) NOT NULL, `delay` bigint NOT NULL, `kit_name` varchar(50) NOT NULL, PRIMARY KEY (kit_name))");
			smt.executeUpdate();
		} catch (SQLException e) {
			plugin.getLogger().info("Erro na criação da tabela no MYSQL: " + e.getLocalizedMessage());
		}
	}

	public boolean insertDelayKit(Player player, String kit, long delay){
		final UUID uuid = player.getUniqueId();
		try (PreparedStatement ps = connection.prepareStatement("INSERT IGNORE INTO kits(uuid, delay, kit_name) VALUES(?,?,?) ON DUPLICATE KEY UPDATE delay = ?")) {
			ps.setString(1, uuid.toString());
			ps.setLong(2, delay);
			ps.setString(3, kit);
			ps.setLong(4, delay);
			ps.executeUpdate();
			return true;
		} catch (SQLException error) {
			error.printStackTrace();
			return false;
		}
	}

	public boolean saveHome(Player player, Home home){
		final UUID uuid = player.getUniqueId();
		final Location location = home.getLocation();
		try (PreparedStatement ps = connection.prepareStatement("INSERT IGNORE INTO homes(uuid, name, is_default, world, x, y, z, pitch, yaw) VALUES(?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE uuid = ?, is_default = ?, world = ?, x = ?, y = ?, z = ?, pitch = ?, yaw = ?")) {
			ps.setString(1, uuid.toString());
			ps.setString(2, home.getName());
			ps.setBoolean(3, home.isDefault());
			ps.setString(4, location.getWorld().getName());
			ps.setInt(5, location.getBlockX());
			ps.setInt(6, location.getBlockY());
			ps.setInt(7, location.getBlockZ());
			ps.setDouble(8, location.getPitch());
			ps.setDouble(9, location.getYaw());

			ps.setString(10, uuid.toString());
			ps.setBoolean(11, home.isDefault());
			ps.setString(12, location.getWorld().getName());
			ps.setInt(13, location.getBlockX());
			ps.setInt(14, location.getBlockY());
			ps.setInt(15, location.getBlockZ());
			ps.setDouble(16, location.getPitch());
			ps.setDouble(17, location.getYaw());
			ps.executeUpdate();
			return true;
		} catch (SQLException error) {
			error.printStackTrace();
			return false;
		}
	}

	public boolean loadKitsDelay(UUID uuid) {
		try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM kits WHERE UUID = ?;")) {
			ps.setString(1, uuid.toString());

			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()){
					final String kitName = rs.getString("kit_name");
					final long delay = rs.getLong("delay");

					if(delay < System.currentTimeMillis()) continue;

					final Kit kit = plugin.getKitController().getKit(kitName);
					if(kit == null) continue;
					final Map<UUID, Long> delayKit = kit.getCooldownMap();

					delayKit.put(uuid, delay);
				}
			}
			return true;
		} catch (SQLException error) {
			error.printStackTrace();
			return false;
		}
	}

	public boolean loadHomes(UUID uuid) {
		final PlayerData data = plugin.getController().getDataPlayer(uuid);

		final HashMap<String, Home> homes = Maps.newHashMap();
		try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM homes WHERE UUID = ?;")) {
			ps.setString(1, uuid.toString());

			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()){
					final String name = rs.getString("name");
					final String worldName = rs.getString("world");
					final World world = Bukkit.getWorld(worldName);
					final boolean isDefault = rs.getBoolean("is_default");
					if(world == null) continue;

					final int x = rs.getInt("x");
					final int y = rs.getInt("y");
					final int z = rs.getInt("z");
					final float pitch = rs.getFloat("pitch");
					final float yaw = rs.getFloat("yaw");
					final Location location = new Location(world, x, y, z, pitch, yaw);
					final Home home = new Home(name, location, isDefault);

					if(isDefault){
						data.setDefaultHome(home);
						continue;
					}

					homes.put(name, new Home(name, location, false));
				}
			}

			data.setHomes(homes);
			return true;
		} catch (SQLException error) {
			error.printStackTrace();
			return false;
		}
	}

	public String getStringFromPlayer(String uuid, String valor) {
		try {
			String sql = "SELECT * FROM players WHERE UUID = ?";
			smt = connection.prepareStatement(sql);
			smt.setString(1, uuid);
			ResultSet result = smt.executeQuery();
			return result.getString(valor);
		} catch (SQLException e) {
			Bukkit.getLogger().info("Método getString retornou nullo");
		}
		return null;
	}

	public Connection getConnectionMySQL() {
		return connection;
	}
	
	public ResultSet getString(String coluna, String valor) {
		try {
			String sql = "SELECT * FROM players WHERE ? = ?";
			smt = connection.prepareStatement(sql);
			smt.setString(1, coluna);
			smt.setString(2, valor);
			return smt.executeQuery();
		} catch (SQLException e) {
			Bukkit.getLogger().info("Método getString retornou nullo");
		}
		return null;
	}

	public void executeUpdateMySQL(String query) {
		try {
			smt = connection.prepareStatement(query);
			smt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean close() {
		try {
			if(getConnectionMySQL() != null) {
				getConnectionMySQL().close();
			}else {
				return false;
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public Connection restart() {
		close();
		return getConnectionMySQL();
	}
}
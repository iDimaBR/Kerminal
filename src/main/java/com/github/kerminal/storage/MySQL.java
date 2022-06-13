package com.github.kerminal.storage;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import com.github.kerminal.Kerminal;
import com.github.kerminal.models.Home;
import com.github.kerminal.models.PlayerData;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

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

	private Gson GSON = new Gson();

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
	
	public void createTable() {
		try {
			smt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS players(`UUID` varchar(36) NOT NULL, `defaultHome` varchar(50) NULL, `homes` longtext)");
			smt.executeUpdate();
		} catch (SQLException e) {
			plugin.getLogger().info("Erro na criação da tabela no MYSQL: " + e.getLocalizedMessage());
		}
	}

	public boolean savePlayer(UUID uuid){
		PlayerData data = plugin.getController().getDataPlayer(uuid);
		if(data == null) return false;

		String query = "UPDATE players SET UUID = ?, defaultHome = ?, homes = ? WHERE UUID = ?";
		if(!contains(uuid))
			query = "INSERT INTO players(`UUID`,`defaultHome`,`homes`) VALUES (?,?,?)";

		try (PreparedStatement ps = connection.prepareStatement(query)) {

			final String homesJson = GSON.toJson(data.getHomes().values());
			ps.setString(1, uuid.toString());
			ps.setString(2, data.getFormatedDefaultHome());
			ps.setString(3, homesJson);
			if (query.contains("WHERE"))
				ps.setString(4, uuid.toString());
			ps.executeUpdate();
			return true;
		} catch (SQLException error) {
			error.printStackTrace();
			return false;
		}
	}

	public boolean loadPlayer(UUID uuid) {
		final PlayerData data = new PlayerData(uuid);
		try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM players WHERE UUID = ?;")) {
			ps.setString(1, uuid.toString());

			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()){
					final String defaultHomeFormated = rs.getString("defaultHome");
					if(defaultHomeFormated != null && defaultHomeFormated.contains(";")) {
						final String[] defaultHomesValues = rs.getString("defaultHome").split(";");
						final World world = Bukkit.getWorld(defaultHomesValues[0]);
						final int x = Integer.parseInt(defaultHomesValues[1]);
						final int y = Integer.parseInt(defaultHomesValues[2]);
						final int z = Integer.parseInt(defaultHomesValues[3]);
						final Location defaultHome = new Location(world, x, y, z);
						data.setDefaultHome(new Home(defaultHome));
					}

					final Type listHomesOBJ = new TypeToken<ArrayList<Home>>() {}.getType();
					final List<Home> listHomes = GSON.fromJson(rs.getString("homes"), listHomesOBJ);

					final HashMap<String, Home> homes = Maps.newHashMap();
					for (Home home : listHomes) {
						String[] homeValues = home.getFormatedLocation().split(";");
						World world2 = Bukkit.getWorld(homeValues[0]);
						int x2 = Integer.parseInt(homeValues[1]);
						int y2 = Integer.parseInt(homeValues[2]);
						int z2 = Integer.parseInt(homeValues[3]);
						home.setLocation(new Location(world2, x2, y2, z2));

						homes.put(home.getName(), home);
					}
					data.setHomes(homes);
				}
			}
			plugin.getController().getDATALIST().put(uuid, data);
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
	
	public boolean contains(UUID uuid){
		try {
			smt = connection.prepareStatement("SELECT `UUID` FROM players WHERE `UUID` = ?");
			smt.setString(1, uuid.toString());
			ResultSet result = smt.executeQuery();
			if(result.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
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
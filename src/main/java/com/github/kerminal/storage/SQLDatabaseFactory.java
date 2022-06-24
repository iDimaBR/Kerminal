package com.github.kerminal.storage;

import com.github.kerminal.Kerminal;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class SQLDatabaseFactory {
    private static final File FILE = new File(Kerminal.getPlugin(Kerminal.class).getDataFolder(), "data");

    public static SQLConnector createConnector(ConfigurationSection section) {
        String databaseType = section.getString("type");

        ConfigurationSection typeSection = section.getConfigurationSection(databaseType);
        switch (databaseType) {
            case "sqlite":
                return buildSQLITE(typeSection).connect();
            case "mysql":
                return buildMYSQL(typeSection).connect();
            default:
                throw new UnsupportedOperationException("database type unsupported!");
        }
    }

    private static SQLiteDatabaseType buildSQLITE(ConfigurationSection typeSection) {
        return SQLiteDatabaseType.builder()
                .file(new File(FILE, typeSection.getString("fileName")))
                .build();
    }

    private static MySQLDatabaseType buildMYSQL(ConfigurationSection typeSection) {
        return MySQLDatabaseType.builder()
                .address(typeSection.getString("address"))
                .username(typeSection.getString("username"))
                .password(typeSection.getString("password"))
                .database(typeSection.getString("database"))
                .build();
    }

}
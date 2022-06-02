package com.github.idimabr.utils;

import com.github.idimabr.Kerminal;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigUtil extends FileConfiguration{

    private Kerminal plugin;
    private boolean isNewFile;
    private File currentDirectory;
    private File file;
    private FileConfiguration fileConfig;

    public ConfigUtil(Kerminal plugin, String directory, String fileName, boolean isNewFile) {
        this.plugin = plugin;
        this.isNewFile = isNewFile;

        createDirectory(directory);
        createFile(directory, fileName);

        fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public void createDirectory(String directory) {
        this.currentDirectory = plugin.getDataFolder();
        if(directory != null) {
            this.currentDirectory = new File(plugin.getDataFolder(), directory.replace("/", File.separator));
            this.currentDirectory.mkdirs();

        }
    }

    public void createFile(String directory, String fileName) {
        file = new File(this.currentDirectory, fileName);
        if(!file.exists()) {
            if(this.isNewFile) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }else {
                plugin.saveResource(directory != null ? directory + File.separator + fileName : fileName, false);
            }
        }
    }

    public FileConfiguration getConfig() {
        return fileConfig;
    }

    public void saveConfig() {
        try {
            fileConfig.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reloadConfig() {
        this.fileConfig.setDefaults(YamlConfiguration.loadConfiguration(file));
    }

    @Override
    public String getString(String path) {
        return getConfig().getString(path).replace("&","§");
    }
    @Override
    public List<String> getStringList(String path) {
        return getConfig().getStringList(path).stream().map($ -> $.replace("&","§")).collect(Collectors.toList());
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return getConfig().getConfigurationSection(path);
    }

    @Override
    public String saveToString() {
        return null;
    }

    @Override
    public void loadFromString(String s) throws InvalidConfigurationException {

    }

    @Override
    protected String buildHeader() {
        return null;
    }
}
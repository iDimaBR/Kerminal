package com.github.idimabr.utils;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class ConfigUtil extends FileConfiguration {

    private final Plugin owningPlugin;
    private final String name;
    protected File rootFile;
    private FileConfiguration configuration;

    public ConfigUtil(Plugin owningPlugin, String name) {
        if (!name.endsWith(".yml")) name += ".yml";

        this.owningPlugin = owningPlugin;
        this.name = name;

        this.rootFile = new File(owningPlugin.getDataFolder(), name);
        load();
    }

    public void load() {
        if (!rootFile.exists()) {
            rootFile.getParentFile().mkdirs();
            owningPlugin.saveResource(name, false);
        }

        this.configuration = new YamlConfiguration();
        try {
            configuration.load(rootFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String s, boolean b) {
        return this.configuration.getBoolean(s, b);
    }

    public List<Byte> getByteList(String s) {
        return this.configuration.getByteList(s);
    }

    public ItemStack getItemStack(String s) {
        return this.configuration.getItemStack(s);
    }

    public OfflinePlayer getOfflinePlayer(String s, OfflinePlayer offlinePlayer) {
        return this.configuration.getOfflinePlayer(s, offlinePlayer);
    }

    public Configuration getRoot() {
        return this.configuration.getRoot();
    }

    public void save() {
        try {
            this.configuration.save(rootFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentPath() {
        return this.configuration.getCurrentPath();
    }

    public List<Map<?, ?>> getMapList(String s) {
        return this.configuration.getMapList(s);
    }

    public ConfigurationSection createSection(String s) {
        return this.configuration.createSection(s);
    }

    public void load(Reader reader) throws java.io.IOException, org.bukkit.configuration.InvalidConfigurationException {
        this.configuration.load(reader);
    }

    public List<Double> getDoubleList(String s) {
        return this.configuration.getDoubleList(s);
    }

    public List<Float> getFloatList(String s) {
        return this.configuration.getFloatList(s);
    }

    public ConfigurationSection createSection(String s, Map<?, ?> map) {
        return this.configuration.createSection(s, map);
    }

    public Vector getVector(String s) {
        return this.configuration.getVector(s);
    }

    public Set<String> getKeys(boolean b) {
        return this.configuration.getKeys(b);
    }

    public List<?> getList(String s, List<?> list) {
        return this.configuration.getList(s, list);
    }

    public List<Integer> getIntegerList(String s) {
        return this.configuration.getIntegerList(s);
    }

    public OfflinePlayer getOfflinePlayer(String s) {
        return this.configuration.getOfflinePlayer(s);
    }

    public <E extends Enum<E>> E getEnum(String path, Class<E> enumClazz) {
        return Enum.valueOf(enumClazz, path);
    }

    public void load(File file) throws java.io.IOException, org.bukkit.configuration.InvalidConfigurationException {
        this.configuration.load(file);
    }

    public boolean isItemStack(String s) {
        return this.configuration.isItemStack(s);
    }

    public boolean isConfigurationSection(String s) {
        return this.configuration.isConfigurationSection(s);
    }

    public boolean isOfflinePlayer(String s) {
        return this.configuration.isOfflinePlayer(s);
    }

    public int getInt(String s) {
        return this.configuration.getInt(s);
    }

    public boolean isVector(String s) {
        return this.configuration.isVector(s);
    }

    public void load(String file) throws java.io.IOException, org.bukkit.configuration.InvalidConfigurationException {
        this.configuration.load(file);
    }

    public Configuration getDefaults() {
        return this.configuration.getDefaults();
    }

    public void setDefaults(Configuration defaults) {
        this.configuration.setDefaults(defaults);
    }

    public Vector getVector(String s, Vector vector) {
        return this.configuration.getVector(s, vector);
    }

    public List<Short> getShortList(String s) {
        return this.configuration.getShortList(s);
    }

    public Map<String, Object> getValues(boolean b) {
        return this.configuration.getValues(b);
    }

    public boolean isInt(String s) {
        return this.configuration.isInt(s);
    }

    public double getDouble(String s, double v) {
        return this.configuration.getDouble(s, v);
    }

    public boolean isLong(String s) {
        return this.configuration.isLong(s);
    }

    public boolean contains(String s) {
        return this.configuration.contains(s);
    }

    public List<String> getStringList(String s) {
        return this.configuration.getStringList(s);
    }

    public ConfigurationSection getConfigurationSection(String s) {
        return this.configuration.getConfigurationSection(s);
    }

    public Object get(String s, Object o) {
        return this.configuration.get(s, o);
    }

    public long getLong(String s) {
        return this.configuration.getLong(s);
    }

    public int getInt(String s, int i) {
        return this.configuration.getInt(s, i);
    }

    public Color getColor(String s) {
        return this.configuration.getColor(s);
    }

    public boolean isBoolean(String s) {
        return this.configuration.isBoolean(s);
    }

    public void addDefault(String path, Object value) {
        this.configuration.addDefault(path, value);
    }

    public void set(String s, Object o) {
        this.configuration.set(s, o);
    }

    public FileConfigurationOptions options() {
        return this.configuration.options();
    }

    public void addDefaults(Configuration defaults) {
        this.configuration.addDefaults(defaults);
    }

    public Object get(String s) {
        return this.configuration.get(s);
    }

    public boolean isList(String s) {
        return this.configuration.isList(s);
    }

    public boolean isColor(String s) {
        return this.configuration.isColor(s);
    }

    public void load(InputStream stream) throws java.io.IOException, org.bukkit.configuration.InvalidConfigurationException {
        this.configuration.load(stream);
    }

    public List<Long> getLongList(String s) {
        return this.configuration.getLongList(s);
    }

    public boolean getBoolean(String s) {
        return this.configuration.getBoolean(s);
    }

    public ItemStack getItemStack(String s, ItemStack itemStack) {
        return this.configuration.getItemStack(s, itemStack);
    }

    public ConfigurationSection getDefaultSection() {
        return this.configuration.getDefaultSection();
    }

    public void save(File file) throws java.io.IOException {
        this.configuration.save(file);
    }

    public String getString(String s, String s1) {
        return this.configuration.getString(s, s1);
    }

    public String getName() {
        return this.configuration.getName();
    }

    public Color getColor(String s, Color color) {
        return this.configuration.getColor(s, color);
    }

    public String saveToString() {
        return this.configuration.saveToString();
    }

    public String getString(String s) {
        return this.configuration.getString(s);
    }

    public boolean isString(String s) {
        return this.configuration.isString(s);
    }

    public List<Boolean> getBooleanList(String s) {
        return this.configuration.getBooleanList(s);
    }

    public void addDefaults(Map<String, Object> defaults) {
        this.configuration.addDefaults(defaults);
    }

    public double getDouble(String s) {
        return this.configuration.getDouble(s);
    }

    public List<?> getList(String s) {
        return this.configuration.getList(s);
    }

    public void loadFromString(String s) throws org.bukkit.configuration.InvalidConfigurationException {
        this.configuration.loadFromString(s);
    }

    @Override
    protected String buildHeader() {
        return null;
    }

    public List<Character> getCharacterList(String s) {
        return this.configuration.getCharacterList(s);
    }

    public boolean isSet(String s) {
        return this.configuration.isSet(s);
    }

    public boolean isDouble(String s) {
        return this.configuration.isDouble(s);
    }

    public ConfigurationSection getParent() {
        return this.configuration.getParent();
    }

    public long getLong(String s, long l) {
        return this.configuration.getLong(s, l);
    }
}
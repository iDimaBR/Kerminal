package com.github.kerminal.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class Utils {

    private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static void setHeaderFooter(Player player, String header, String footer) {
        try {
            Class<?> iChatBaseComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
            Class<?> chatSerializer = iChatBaseComponent.getDeclaredClasses()[0];
            Class<?> packetOutHeaderFooter = Class.forName("net.minecraft.server." + version + ".PacketPlayOutPlayerListHeaderFooter");
            Method a = chatSerializer.getMethod("a", String.class);
            header = ChatColor.translateAlternateColorCodes('&', header);
            footer = ChatColor.translateAlternateColorCodes('&', footer);
            Object chatBaseCompHeader = a.invoke(null, header.startsWith("{") ? header : "{\"text\":\"" + header + "\"}");
            Object chatBaseCompFooter = a.invoke(null, footer.startsWith("{") ? footer : "{\"text\":\"" + footer + "\"}");
            Object headerFooterPacket = packetOutHeaderFooter.getDeclaredConstructor(iChatBaseComponent).newInstance(chatBaseCompHeader);
            setDeclaredField(headerFooterPacket, "b", chatBaseCompFooter);
            sendPacket(player, headerFooterPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Player getPlayer(String playerName) {
        Player testPlayer = Bukkit.getPlayerExact(playerName);
        if (testPlayer != null) return testPlayer;

        try {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            if (!offlinePlayer.hasPlayedBefore()) return null;

            Class<?> PlayerInteractManagerClass = getNMSClass("PlayerInteractManager");
            Class<?> MinecraftServerClass = getNMSClass("MinecraftServer");
            Class<?> EntityPlayerClass = getNMSClass("EntityPlayer");
            Class<?> WorldServerClass = getNMSClass("WorldServer");
            Class<?> WorldClass = getNMSClass("World");
            Class<?> WorldTypeClass;
            Class<?> gameProfileClass;
            Method getServer;
            Method getWorldServer;
            Method getBukkitEntity;
            Constructor<?> gameProfileConstructor;
            Constructor<?> entityPlayerConstructor;
            Constructor<?> playerInteractManagerConstructor;
            WorldTypeClass = int.class;
            gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            gameProfileConstructor = gameProfileClass.getConstructor(UUID.class, String.class);
            getServer = MinecraftServerClass.getMethod("getServer");
            getWorldServer = MinecraftServerClass.getMethod("getWorldServer", WorldTypeClass);
            getBukkitEntity = EntityPlayerClass.getMethod("getBukkitEntity");
            entityPlayerConstructor = EntityPlayerClass.getConstructor(MinecraftServerClass, WorldServerClass, gameProfileClass, PlayerInteractManagerClass);
            playerInteractManagerConstructor = PlayerInteractManagerClass.getConstructor(WorldClass);
            Object uuid = offlinePlayer.getUniqueId();
            Object gameProfile = gameProfileConstructor.newInstance(uuid, playerName);
            Object minecraftServer = getServer.invoke(null);
            Object worldServer = getWorldServer.invoke(minecraftServer, 0);
            Object playerInteractManager = playerInteractManagerConstructor.newInstance(worldServer);
            Object entityPlayer = entityPlayerConstructor.newInstance(minecraftServer, worldServer, gameProfile, playerInteractManager);

            Player player = (Player) getBukkitEntity.invoke(entityPlayer);
            player.loadData();
            return player;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendTitle(Player player, String title, String subtitle){
        try {
            Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            Object titleChat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
            Object enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            Object subtitleChat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");

            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object titlePacket = titleConstructor.newInstance(enumTitle, titleChat, 30, 50, 30);
            Object subtitlePacket = titleConstructor.newInstance(enumSubtitle, subtitleChat, 30, 40, 30);

            sendPacket(player, titlePacket);
            sendPacket(player, subtitlePacket);

        }catch (Exception ignored){}
    }

    static void sendPacket(Player player, Object packet) {
        try {
            Object handle  = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setDeclaredField(Object o, String name, Object value) {
        try {
            Field f = o.getClass().getDeclaredField(name);
            f.setAccessible(true);
            f.set(o, value);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static <E extends Enum<E>> boolean isValidEnum(Class<E> validateEnum, String name) {
        try {
            Enum.valueOf(validateEnum, name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

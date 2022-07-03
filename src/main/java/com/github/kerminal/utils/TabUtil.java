package com.github.kerminal.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.github.kerminal.utils.Utils.sendPacket;

public class TabUtil {

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
        }
        catch(Exception e) {
            e.printStackTrace();
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
}

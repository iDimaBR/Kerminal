package com.github.kerminal.utils;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PermissionUtil {

    public static int getNumberPermission(Player player, String checkPermission){
        for (PermissionAttachmentInfo $ : player.getEffectivePermissions()) {
            String permission = $.getPermission();
            if(permission.startsWith(checkPermission)){
                String subNumber = permission.substring(permission.lastIndexOf('.') + 1);
                if(!NumberUtils.isNumber(subNumber)) return 0;
                return Integer.parseInt(subNumber);
            }
        }
        return 0;
    }
}

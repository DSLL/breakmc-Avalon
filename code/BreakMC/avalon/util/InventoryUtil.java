package code.BreakMC.avalon.util;

import java.lang.reflect.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class InventoryUtil
{
    private static String version;
    private static final Field iinvField;
    private static final Field titleField;
    private static final Field handleField;
    private static final Field containerCounterField;
    private static final Constructor<?> openWindowPacketConstructor;
    private static final Field playerConnectionField;
    private static final Method sendPacket;
    
    static {
        final String[] parts = Bukkit.class.getName().split(".");
        if (parts.length == 4) {
            InventoryUtil.version = "";
        }
        else {
            InventoryUtil.version = "." + parts[4];
        }
        Field tiinv = null;
        Field ttitle = null;
        Field thandle = null;
        Field tcontainerCounter = null;
        Constructor<?> topenWindowPacket = null;
        Field tplayerConnection = null;
        Method tsendPacket;
        try {
            tiinv = getVersionedClass("org.bukkit.craftbukkit.inventory.CraftInventory").getDeclaredField("inventory");
            tiinv.setAccessible(true);
            ttitle = getVersionedClass("org.bukkit.craftbukkit.inventory.CraftInventoryCustom$MinecraftInventory").getDeclaredField("title");
            ttitle.setAccessible(true);
            thandle = getVersionedClass("org.bukkit.craftbukkit.entity.CraftEntity").getDeclaredField("handle");
            thandle.setAccessible(true);
            tcontainerCounter = getVersionedClass("net.minecraft.server.EntityPlayer").getDeclaredField("containerCounter");
            tcontainerCounter.setAccessible(true);
            thandle = getVersionedClass("org.bukkit.craftbukkit.entity.CraftEntity").getDeclaredField("handle");
            thandle.setAccessible(true);
            topenWindowPacket = getVersionedClass("net.minecraft.server.PacketPlayOutOpenWindow").getDeclaredConstructor(Integer.TYPE, Integer.TYPE, String.class, Integer.TYPE, Boolean.TYPE);
            topenWindowPacket.setAccessible(true);
            tplayerConnection = getVersionedClass("net.minecraft.server.EntityPlayer").getDeclaredField("playerConnection");
            tplayerConnection.setAccessible(true);
            tsendPacket = getVersionedClass("net.minecraft.server.PlayerConnection").getDeclaredMethod("sendPacket", topenWindowPacket.getDeclaringClass().getSuperclass());
            tsendPacket.setAccessible(true);
        }
        catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
        iinvField = tiinv;
        titleField = ttitle;
        handleField = thandle;
        containerCounterField = tcontainerCounter;
        openWindowPacketConstructor = topenWindowPacket;
        playerConnectionField = tplayerConnection;
        sendPacket = tsendPacket;
    }
    
    private static Class<?> getVersionedClass(final String className) throws ClassNotFoundException {
        if (className.startsWith("net.minecraft.server")) {
            if (InventoryUtil.version.isEmpty()) {
                return Class.forName(className);
            }
            return Class.forName(String.format("net.minecraft.server%s.%s", InventoryUtil.version, className.substring("net.minecraft.server.".length())));
        }
        else {
            if (!className.startsWith("org.bukkit.craftbukkit")) {
                throw new IllegalArgumentException("Not a versioned class!");
            }
            if (InventoryUtil.version.isEmpty()) {
                return Class.forName(className);
            }
            return Class.forName(String.format("net.minecraft.server%s.%s", InventoryUtil.version, className.substring("org.bukkit.craftbukkit.".length())));
        }
    }
    
    public static void renameInventory(final Player player, final Inventory inventory, final String title) {
        try {
            final Object iinv = InventoryUtil.iinvField.get(inventory);
            InventoryUtil.titleField.set(iinv, title);
            final Object handle = InventoryUtil.handleField.get(player);
            final Integer containerCounter = (Integer)InventoryUtil.containerCounterField.get(handle);
            final Object playerConnection = InventoryUtil.playerConnectionField.get(handle);
            final Object packet = InventoryUtil.openWindowPacketConstructor.newInstance(containerCounter, 0, title, inventory.getSize(), false);
            InventoryUtil.sendPacket.invoke(playerConnection, packet);
        }
        catch (Exception ex) {}
    }
}

package code.BreakMC.avalon.util;

import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import org.spigotmc.*;
import net.minecraft.server.v1_7_R4.*;
import java.lang.reflect.*;

public class Title
{
    private static int VERSION;
    
    static {
        Title.VERSION = 47;
    }
    
    public static void sendTitle(final Player p, final String title) {
        if (((CraftPlayer)p).getHandle().playerConnection.networkManager.getVersion() < Title.VERSION) {
            return;
        }
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TITLE, ChatSerializer.a("{\"text\": \"\"}").a(title)));
    }
    
    public static void sendSubTitle(final Player p, final String subtitle) {
        if (((CraftPlayer)p).getHandle().playerConnection.networkManager.getVersion() < Title.VERSION) {
            return;
        }
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.SUBTITLE, ChatSerializer.a("{\"text\": \"\"}").a(subtitle)));
    }
    
    public static void sendTimings(final Player p, final int fadeIn, final int stay, final int fadeOut) {
        if (((CraftPlayer)p).getHandle().playerConnection.networkManager.getVersion() < Title.VERSION) {
            return;
        }
        try {
            final Object handle = getHandle(p);
            final Object connection = getField(handle.getClass(), "playerConnection").get(handle);
            final Object packet = ProtocolInjector.PacketTitle.class.getConstructor(ProtocolInjector.PacketTitle.Action.class, Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(ProtocolInjector.PacketTitle.Action.TIMES, fadeIn, stay, fadeOut);
            getMethod(connection.getClass(), "sendPacket", (Class<?>[])new Class[0]).invoke(connection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static boolean ClassListEqual(final Class<?>[] l1, final Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length) {
            return false;
        }
        for (int i = 0; i < l1.length; ++i) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }
        return equal;
    }
    
    private static Field getField(final Class<?> clazz, final String name) {
        try {
            final Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Method getMethod(final Class<?> clazz, final String name, final Class<?>... args) {
        Method[] methods;
        for (int length = (methods = clazz.getMethods()).length, i = 0; i < length; ++i) {
            final Method m = methods[i];
            if (m.getName().equals(name) && (args.length == 0 || ClassListEqual(args, m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }
    
    private static Object getHandle(final Object obj) {
        try {
            return getMethod(obj.getClass(), "getHandle", (Class<?>[])new Class[0]).invoke(obj, new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void reset(final Player p) {
        if (((CraftPlayer)p).getHandle().playerConnection.networkManager.getVersion() < Title.VERSION) {
            return;
        }
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.RESET));
    }
    
    public static void clear(final Player p) {
        if (((CraftPlayer)p).getHandle().playerConnection.networkManager.getVersion() < Title.VERSION) {
            return;
        }
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.CLEAR));
    }
}

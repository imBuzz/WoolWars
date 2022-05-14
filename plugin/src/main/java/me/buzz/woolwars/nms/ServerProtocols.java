package me.buzz.woolwars.nms;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerProtocols {

    public static String getServerVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1);
        if (version.contains("v1_17") && !Bukkit.getServer().getVersion().matches("(.*)1\\.17\\.\\d(.*)"))
            version = "v1_17_R0";
        if (version.contains("v1_18") && !Bukkit.getServer().getVersion().matches("(.*)1\\.18\\.\\d(.*)"))
            version = "v1_18_R0";
        return version;
    }

    public static INMSHandler getNmsHandler(JavaPlugin javaPlugin) {
        try {
            return (INMSHandler) Class.forName("me.buzz.woolwars.nms." + getServerVersion() + ".NmsHandler").getConstructor().newInstance();
        } catch (Exception exc) {
            exc.printStackTrace();
            javaPlugin.getLogger().severe("You are running PetsReloaded on an unsupported NMS version " + getServerVersion() + " stopping...");
            return null;
        }
    }

}

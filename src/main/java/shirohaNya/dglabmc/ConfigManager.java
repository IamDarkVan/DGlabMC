package shirohaNya.dglabmc;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import static shirohaNya.dglabmc.DGlabMC.plugin;


public class ConfigManager {
    private static FileConfiguration cfg = plugin.getConfig();

    public static void setConfigValue(String name, Object value){
        cfg.set(name, value);
    }

    public static void reload() {
        cfg = plugin.getConfig();
    }

    public static String getIp() {
        return cfg.getString("ip");
    }

    public static int getPort() {
        return cfg.getInt("port");
    }

    public static boolean isAutoRunServer() {
        return cfg.getBoolean("autoRunServer");
    }

    public static boolean isLogOutputMessage() {
        return cfg.getBoolean("logOutputMessage");
    }

    public static boolean isLogInputMessage() {
        return cfg.getBoolean("logInputMessage");
    }

    public static ConfigurationSection getScriptConfigPart(String scripts) {
        return cfg.getConfigurationSection(scripts);
    }
}

package shirohaNya.dglabmc;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getLogger;
import static shirohaNya.dglabmc.DGlabMC.plugin;
import static shirohaNya.dglabmc.utils.DGlabUtils.overwriteConfig;


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

    public static @NotNull ConfigurationSection getScriptConfigPart(String scripts) {
        ConfigurationSection section = cfg.getConfigurationSection(scripts);
        if (section != null) return section;
        getLogger().warning("无法加载config.yml中的" + scripts);
        getLogger().warning("正在覆盖现有config.yml");
        overwriteConfig();
        reload();
        return getScriptConfigPart(scripts);
    }
}

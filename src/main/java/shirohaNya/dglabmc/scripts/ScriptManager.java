package shirohaNya.dglabmc.scripts;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import shirohaNya.dglabmc.ConfigManager;
import shirohaNya.dglabmc.api.Script;

import java.util.*;


import static org.bukkit.Bukkit.getLogger;
import static shirohaNya.dglabmc.ConfigManager.getScriptConfigPart;
import static shirohaNya.dglabmc.utils.DGlabUtils.overwriteConfig;

public class ScriptManager {

    private static final HashMap<String, Script> scriptMap = new HashMap<>();
    //["script1": {"default": false, "time": 5, "replace": true},"script2": {...}, ...]
    private static @NotNull ConfigurationSection scriptConfigs = getScriptConfigPart("scripts");
    @Getter
    private static final HashSet<Script> defaultScripts = new HashSet<>();

    private ScriptManager() throws Exception {
        throw new Exception("管理类不允许实例化");
    }

    public static void putDefaultScript(Script script) {
        defaultScripts.add(script);
    }

    public static Set<String> getScriptNameSet() {
        return scriptMap.keySet();
    }

    public static Collection<Script> getScriptSet() {
        return scriptMap.values();
    }

    public static @NotNull ConfigurationSection getScriptConfig(@NotNull String name) {
        ConfigurationSection cfg = scriptConfigs.getConfigurationSection(name);
        if (cfg != null) return cfg;
        getLogger().warning("无法加载config.yml中的scripts");
        getLogger().warning("正在覆盖现有config.yml");
        overwriteConfig();
        ConfigManager.reload();
        reloadScriptConfig();
        return getScriptConfig(name);
    }

    public static void reloadScriptConfig() {
        scriptConfigs = getScriptConfigPart("scripts");
    }

    public static @NotNull Script getScript(@NotNull String name) {
        if (!scriptMap.containsKey(name)) throw new RuntimeException();
        return scriptMap.get(name);
    }

    public static boolean isScriptExist(@NotNull String name) {
        return scriptMap.containsKey(name);
    }

    public static void registerScript(@NotNull String name, @NotNull Script script) {
        scriptMap.put(name, script);
    }
}

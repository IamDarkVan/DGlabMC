package shirohaNya.dglabmc.scripts;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.api.Script;

import java.util.*;


import static shirohaNya.dglabmc.ConfigManager.getScriptConfigPart;

public class ScriptManager {

    private static final HashMap<String, Script> scriptMap = new HashMap<>();
    //["script1": {"default": false, "time": 5, "replace": true},"script2": {...}, ...]
    private static ConfigurationSection scriptConfigs = getScriptConfigPart("scripts");
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

    public static @Nullable ConfigurationSection getScriptConfig(@NotNull String name) {
        return scriptConfigs.getConfigurationSection(name);
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

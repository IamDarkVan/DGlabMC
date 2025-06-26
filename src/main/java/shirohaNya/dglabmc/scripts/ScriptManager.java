package shirohaNya.dglabmc.scripts;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


import static shirohaNya.dglabmc.ConfigManager.getMapList;

public class ScriptManager {

    private static final HashMap<String, Script> scriptMap = new HashMap<>();
    //["script1": {"default": false, "time": 5, "replace": true},"script2": {...}, ...]
    private static final List<Map<?, ?>> scriptConfigs = getMapList("scripts");
    @Getter
    private static final HashSet<Script> defaultScripts = new HashSet<>();

    private ScriptManager() {

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

    public static @Nullable Map<?, ?> getConfig(@NotNull String name) {
        return scriptConfigs.stream().filter(script -> script.containsKey(name)).findFirst().orElse(null);
    }

    public static @Nullable Script getScript(@NotNull String name) {
        return scriptMap.get(name);
    }

    public static void registerScript(@NotNull String name, @NotNull Script script) {
        scriptMap.put(name, script);
    }
}

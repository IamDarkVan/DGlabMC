package shirohaNya.dglabmc.scripts;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ScriptManager {
    @Getter
    private static final HashMap<String, Script> scriptMap = new HashMap<>();
    private ScriptManager(){

    }

    public static Script getScript(@NotNull String name){
        assert scriptMap.containsKey(name);
        return scriptMap.get(name);
    }
    public static void registerScript(@NotNull String name, @NotNull Script script){
        scriptMap.put(name,script);
    }
}

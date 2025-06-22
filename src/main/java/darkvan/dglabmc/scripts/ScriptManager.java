package darkvan.dglabmc.scripts;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ScriptManager {
    private final HashMap<String, ScriptAbstract> scriptMap = new HashMap<>();
    private static ScriptManager instance;
    private ScriptManager(){

    }

    public static ScriptManager getScriptManager(){
        if (instance == null) instance = new ScriptManager();
        return instance;
    }

    public ScriptAbstract getScript(@NotNull String name){
        assert scriptMap.containsKey(name);
        return scriptMap.get(name);
    }

    public Set<String> getNameSet(){
        return scriptMap.keySet();
    }

    public Collection<ScriptAbstract> getScriptSet(){
        return scriptMap.values();
    }

    public void registerScript(@NotNull String name, @NotNull ScriptAbstract script){
        scriptMap.put(name,script);
    }
}

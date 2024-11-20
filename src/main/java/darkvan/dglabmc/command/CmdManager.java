package darkvan.dglabmc.command;

import darkvan.dglabmc.command.cmds.Command;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.BiFunction;

@Getter
public class CmdManager {
    private final HashMap<String, BiFunction<CommandSender, String[], Command>> commandMap = new HashMap<>();
    private static CmdManager instance;
    private CmdManager(){

    }
    public static CmdManager getCmdManager(){
        if (instance == null) instance = new CmdManager();
        return instance;
    }

    public Command getCommand(@NotNull String name, @NotNull CommandSender sender, @NotNull String[] args){
        return commandMap.containsKey(name) ? commandMap.get(name).apply(sender, args) : commandMap.get("help").apply(sender, args);
    }
    public void registerCommand(@NotNull String name, @NotNull BiFunction<CommandSender, String[], Command> cmd){
        commandMap.put(name, cmd);
    }
}

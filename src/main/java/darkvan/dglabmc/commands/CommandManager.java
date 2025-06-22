package darkvan.dglabmc.commands;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.BiFunction;

@Getter
public class CommandManager {
    private final HashMap<String, BiFunction<CommandSender, String[], CommandAbstract>> commandMap = new HashMap<>();
    private static CommandManager instance;
    private CommandManager(){

    }
    public static CommandManager getCmdManager(){
        if (instance == null) instance = new CommandManager();
        return instance;
    }

    public CommandAbstract getCommand(@NotNull String name, @NotNull CommandSender sender, @NotNull String[] args){
        return commandMap.containsKey(name) ? commandMap.get(name).apply(sender, args) : commandMap.get("help").apply(sender, args);
    }
    public void registerCommand(@NotNull String name, @NotNull BiFunction<CommandSender, String[], CommandAbstract> cmd){
        commandMap.put(name, cmd);
    }
}

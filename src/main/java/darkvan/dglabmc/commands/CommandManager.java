package darkvan.dglabmc.commands;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.BiFunction;


public class CommandManager {
    @Getter
    private static final HashMap<String, BiFunction<CommandSender, String[], Command>> commandMap = new HashMap<>();
    private CommandManager(){

    }

    public static Command getCommand(@NotNull String name, @NotNull CommandSender sender, @NotNull String[] args){
        return commandMap.containsKey(name) ? commandMap.get(name).apply(sender, args) : commandMap.get("help").apply(sender, args);
    }
    public static void registerCommand(@NotNull String name, @NotNull BiFunction<CommandSender, String[], Command> cmd){
        commandMap.put(name, cmd);
    }
}

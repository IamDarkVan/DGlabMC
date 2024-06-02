package darkvan.dglabmc.command;

import darkvan.dglabmc.command.cmds.*;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class CmdFactory {
    private static final Map<String, BiFunction<CommandSender, String[], Command>> commandMap = new HashMap<>();

    private final Command command;
    public CmdFactory(@NotNull String name, @NotNull CommandSender sender, @NotNull String[] args) {
        this.command = commandMap.containsKey(name) ? commandMap.get(name).apply(sender, args) : commandMap.get("help").apply(sender, args);
    }
    public static void registerCommand(@NotNull String name, @NotNull BiFunction<CommandSender, String[], Command> cmd){
        commandMap.put(name, cmd);
    }

    public boolean execute(){
        return command.execute();
    }

    public List<String> tabComplete(){
        return command.tabComplete();
    }
}

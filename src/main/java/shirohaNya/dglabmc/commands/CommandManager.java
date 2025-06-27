package shirohaNya.dglabmc.commands;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import shirohaNya.dglabmc.api.Command;

import java.util.HashMap;
import java.util.function.BiFunction;


public class CommandManager {
    @Getter
    private static final HashMap<String, BiFunction<CommandSender, String[], Command>> commandMap = new HashMap<>();

    private CommandManager() throws Exception {
        throw new Exception("管理类不允许实例化");
    }

    public static Command getCommand(@NotNull String name, @NotNull CommandSender sender, @NotNull String[] args) {
        return commandMap.containsKey(name) ? commandMap.get(name).apply(sender, args) : commandMap.get("help").apply(sender, args);
    }

    public static void registerCommand(@NotNull String name, @NotNull BiFunction<CommandSender, String[], Command> cmd) {
        commandMap.put(name, cmd);
    }
}

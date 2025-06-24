package shirohaNya.dglabmc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static shirohaNya.dglabmc.commands.CommandManager.getCommand;
import static shirohaNya.dglabmc.utils.CommandUtils.getCommandList;

public class CommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) return getCommandList(sender);
        return getCommand(args[0].toLowerCase(), sender, args).tabComplete();
    }
}

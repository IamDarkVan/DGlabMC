package darkvan.dglabmc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static darkvan.dglabmc.utils.CommandUtils.commandList;

public class CmdTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args){
        if (args.length == 1) return commandList(sender);
        return new CmdFactory(args[0].toLowerCase(),sender, args).tabComplete();
    }
}

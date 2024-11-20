package darkvan.dglabmc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static darkvan.dglabmc.command.CmdManager.getCmdManager;
import static darkvan.dglabmc.utils.CommandUtils.*;

public class CmdTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args){
        if (args.length == 1) return getCommandList(sender);
        return getCmdManager().getCommand(args[0].toLowerCase(),sender, args).tabComplete();
    }
}

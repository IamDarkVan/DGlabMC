package darkvan.dglabmc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static darkvan.dglabmc.utils.CommandUtils.sendHelp;

public class CmdExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) return sendHelp(sender);
        return new CmdFactory(args[0].toLowerCase(),sender, args).execute();
    }
}

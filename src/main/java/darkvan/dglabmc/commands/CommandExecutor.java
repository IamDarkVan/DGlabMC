package darkvan.dglabmc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static darkvan.dglabmc.commands.CommandManager.getCommand;
import static darkvan.dglabmc.utils.CommandUtils.sendHelp;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) return sendHelp(sender);
        return getCommand(args[0].toLowerCase(),sender, args).execute();
    }
}

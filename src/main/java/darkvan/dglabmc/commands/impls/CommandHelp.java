package darkvan.dglabmc.commands.impls;

import darkvan.dglabmc.commands.CommandException;
import darkvan.dglabmc.commands.CommandAbstract;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static darkvan.dglabmc.utils.CommandUtils.sendHelp;


public class CommandHelp extends CommandAbstract {

    public CommandHelp(@NotNull CommandSender sender, @Nullable String[] args) {
        super("help", sender, args, null, null, "/dglab help -- 显示帮助", null);
    }

    @Override
    protected void errorHandle() throws CommandException {
    }

    @Override
    protected void run() {
        sendHelp(sender);
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

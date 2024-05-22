package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static darkvan.dglabmc.utils.DGlabUtils.sendHelp;


public class CommandHelp extends Command{

    public CommandHelp(@NotNull CommandSender sender, @NotNull String[] args, @Nullable String perm) {
        super(sender, args,null,null, "/dglab help -- 显示帮助", perm);
    }

    @Override
    protected void errorHandle() throws CmdException { }

    @Override
    protected void run() {
        sendHelp(sender);
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

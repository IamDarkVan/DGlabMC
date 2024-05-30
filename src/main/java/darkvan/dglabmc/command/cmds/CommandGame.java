package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandGame extends Command{

    public CommandGame(@NotNull String command, @NotNull CommandSender sender, @NotNull String[] args, @Nullable Integer min, @Nullable Integer max, @Nullable String usage, @Nullable String perm) {
        super("game", sender, args, min, max, "/dglab game [clientId|player] <game> (set|enable|disable) [settings]", perm);
    }

    @Override
    protected void errorHandle() throws CmdException {

    }

    @Override
    protected void run() {

    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

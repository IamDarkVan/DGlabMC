package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static darkvan.dglabmc.DGlabMC.clients;

public class CommandList extends Command{

    public CommandList(@NotNull CommandSender sender, @NotNull String[] args) {
        super("list", sender, args,null,null, "/dglab list -- 显示app列表", "dglab.list");
    }

    @Override
    protected void errorHandle() throws CmdException { }

    @Override
    protected void run() {
        clients.forEach(client -> sender.sendMessage(client.getClientId()));
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

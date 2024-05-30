package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static darkvan.dglabmc.DGlabMC.clients;


public class CommandBindList extends Command{
    public CommandBindList(@NotNull CommandSender sender, @NotNull String[] args, @Nullable String perm) {
        super("bind-list",sender, args, null,null,"/dglab bind-list -- 查询绑定app列表", perm);
    }

    @Override
    protected void errorHandle() throws CmdException { }

    @Override
    protected void run() {
        clients.forEach(client -> {if (client.getPlayer() != null) sender.sendMessage(client.getPlayer().getName() + " <-> " + client.getClientId());});
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

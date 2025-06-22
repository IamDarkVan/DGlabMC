package darkvan.dglabmc.commands.impls;

import darkvan.dglabmc.commands.CommandException;
import darkvan.dglabmc.commands.CommandAbstract;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static darkvan.dglabmc.DGlabMC.clients;


public class CommandBindList extends CommandAbstract {
    public CommandBindList(@NotNull CommandSender sender, @Nullable String[] args) {
        super("bind-list", sender, args, null, null, "/dglab bind-list -- 查询已绑定的app列表", "dglab.list");
    }

    @Override
    protected void errorHandle() throws CommandException {
    }

    @Override
    protected void run() {
        clients.forEach(client -> {if (client.getPlayer() != null) sender.sendMessage(client.getPlayer().getName() + " <-> " + client.getClientId());});
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

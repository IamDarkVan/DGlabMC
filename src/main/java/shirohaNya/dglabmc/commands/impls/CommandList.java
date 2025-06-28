package shirohaNya.dglabmc.commands.impls;

import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.commands.CommandAbstract;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static shirohaNya.dglabmc.client.ClientManager.clients;

public class CommandList extends CommandAbstract {

    public CommandList(@NotNull CommandSender sender, @Nullable String[] args) {
        super("list", sender, args, null, null, "/dglab list -- 显示app列表", "dglab.list");
    }

    @Override
    protected void errorHandle() throws CommandException {
    }

    @Override
    protected void run() {
        sender.sendMessage("========== 绑定列表 ==========");
        clients.forEach(client -> {
            if (client.getPlayer() != null)
                sender.sendMessage(client.getTargetId() + " <-> " + client.getPlayer().getName());
            else
                sender.sendMessage(client.getTargetId() + " 未绑定");
        });
        sender.sendMessage("===============================");
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

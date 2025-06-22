package shirohaNya.dglabmc.commands.impls;

import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.commands.CommandAbstract;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static shirohaNya.dglabmc.DGlabMC.plugin;

public class CommandServerStop extends CommandAbstract {
    public CommandServerStop(@NotNull CommandSender sender, @Nullable String[] args) {
        super("server-stop", sender, args, null, null, "/dglab server-stop -- 关闭目前WebSocket服务器", "dglab.server.stop");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (plugin.mcWebSocketServer == null) throw new CommandException("服务器未在运行");
    }

    @Override
    protected void run() {
        plugin.mcWebSocketServer.stop();
        plugin.mcWebSocketServer = null;
        sender.sendMessage("成功停止WebSocket服务器");
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

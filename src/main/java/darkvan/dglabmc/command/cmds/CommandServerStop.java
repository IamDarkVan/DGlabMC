package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static darkvan.dglabmc.DGlabMC.plugin;

public class CommandServerStop extends Command {
    public CommandServerStop(@NotNull CommandSender sender, @NotNull String[] args) {
        super("server-stop", sender, args, null, null, "/dglab server-stop -- 关闭目前WebSocket服务器", "dglab.server.stop");
    }

    @Override
    protected void errorHandle() throws CmdException {
        if (plugin.mcWebSocketServer == null) throw new CmdException("服务器未在运行");
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

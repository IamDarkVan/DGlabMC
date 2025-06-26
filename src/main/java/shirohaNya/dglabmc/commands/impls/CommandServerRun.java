package shirohaNya.dglabmc.commands.impls;

import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.commands.CommandAbstract;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static shirohaNya.dglabmc.ConfigManager.getPort;
import static shirohaNya.dglabmc.ConfigManager.setConfigValue;
import static shirohaNya.dglabmc.DGlabMC.plugin;
import static shirohaNya.dglabmc.utils.DGlabUtils.runWebSocketServer;

public class CommandServerRun extends CommandAbstract {
    private Integer port;

    public CommandServerRun(@NotNull CommandSender sender, @Nullable String[] args) {
        super("server-run", sender, args, null, 2, "/dglab server-run [port] -- 启动WebSocket服务器 不填端口默认config", "dglab.server.run");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (plugin.mcWebSocketServer != null) throw new CommandException("服务器已在运行,请先停止当前服务器");
        if (length == 2 && !args[1].matches("\\d++")) sender.sendMessage("端口号必须为纯数字");
        this.port = length == 1 ? getPort() : Integer.parseInt(args[1]);
        if (port < 0 || port > 65535) throw new CommandException("端口号必须为0~65535");
    }

    @Override
    protected void run() {
        setConfigValue("port", port);
        runWebSocketServer(port);
        sender.sendMessage("成功运行WebSocket服务器 端口号: " + port);
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

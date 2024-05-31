package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.Client;
import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static darkvan.dglabmc.DGlabMC.clients;
import static darkvan.dglabmc.utils.ClientUtils.getClient;
import static darkvan.dglabmc.utils.ClientUtils.isClientExist;

public class CommandSendMsg extends Command {
    public CommandSendMsg(@NotNull CommandSender sender, @NotNull String[] args) {
        super("send-msg", sender, args, 3, null, "/dglab send-msg <clientId> <message> -- 直接向app发送消息(可空格 不推荐使用)", "dglab.send.msg");
    }

    @Override
    protected void errorHandle() throws CmdException {
        if (!isClientExist(args[1])) throw new CmdException("未找到客户端");
    }

    @Override
    protected void run() {
        getClient(args[1]).output(String.join(" ", Arrays.copyOfRange(args, 2, length)));
        sender.sendMessage("已成功发送" + String.join(" ", Arrays.copyOfRange(args, 2, length)));
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return clients.stream().map(Client::getClientId).collect(Collectors.toList());
        return null;
    }
}

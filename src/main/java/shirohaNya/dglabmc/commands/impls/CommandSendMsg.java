package shirohaNya.dglabmc.commands.impls;

import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.commands.CommandAbstract;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static shirohaNya.dglabmc.client.ClientManager.getClient;
import static shirohaNya.dglabmc.client.ClientManager.isClientExist;
import static shirohaNya.dglabmc.utils.CommandUtils.getClientList;

public class CommandSendMsg extends CommandAbstract {
    public CommandSendMsg(@NotNull CommandSender sender, @Nullable String[] args) {
        super("send-msg", sender, args, 3, null, "/dglab send-msg <clientId> <message> -- 直接向app发送消息(可空格 不推荐使用)", "dglab.send.msg");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (!isClientExist(args[1])) throw new CommandException("未找到客户端");
    }

    @Override
    protected void run() {
        getClient(args[1]).output(String.join(" ", Arrays.copyOfRange(rawArgs, 2, length)));
        sender.sendMessage("已成功发送" + String.join(" ", Arrays.copyOfRange(rawArgs, 2, length)));
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return getClientList(sender);
        return null;
    }
}

package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static darkvan.dglabmc.utils.ClientUtils.getClient;
import static darkvan.dglabmc.utils.ClientUtils.isClientExist;
import static darkvan.dglabmc.utils.CommandUtils.getClientList;
import static darkvan.dglabmc.utils.DGlabUtils.toDGJson;

public class CommandSendDGJson extends Command {

    public CommandSendDGJson(@NotNull CommandSender sender, @NotNull String[] args) {
        super("send-dgjson", sender, args, 6, 6, "/dglab send-dgjson <clientId> <typ> <cid> <tid> <msg>-- 直接向app发送DGJson(不推荐使用)", "dglab.send.dgjson");
    }

    @Override
    protected void errorHandle() throws CmdException {
        if (!isClientExist(args[1])) throw new CmdException("未找到客户端");
    }

    @Override
    protected void run() {
        getClient(args[1]).output(toDGJson(Arrays.copyOfRange(rawArgs, 2, 6)));
        sender.sendMessage("已成功发送" + toDGJson(Arrays.copyOfRange(rawArgs, 2, 6)));
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return getClientList(sender);
        return null;
    }
}

package darkvan.dglabmc.command.cmds;


import darkvan.dglabmc.Client;
import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static darkvan.dglabmc.utils.ClientUtils.getClient;
import static darkvan.dglabmc.utils.ClientUtils.isClientExist;
import static darkvan.dglabmc.utils.CommandUtils.getPlayerAndClientList;
import static org.bukkit.Bukkit.getPlayer;

public class CommandInfo extends Command {


    private Client client;

    public CommandInfo(@NotNull CommandSender sender, @Nullable String[] args) {
        super("info", sender, args, null, 2, "/dglab info [clientId|player] 查询app信息", "dglab.info");
    }

    @Override
    protected void errorHandle() throws CmdException {
        if (length == 1) {
            if (!(sender instanceof Player)) throw new CmdException("服务器后台请使用 /dglab info <clientId|player>");
            Player player = (Player) sender;
            if (!isClientExist(player)) throw new CmdException("你还没有绑定的app");
            this.client = getClient(player);
        }
        if (length == 2) {
            if (!isClientExist(args[1]) && isClientExist(getPlayer(args[1]))) throw new CmdException("客户端不存在或玩家未绑定");
            this.client = isClientExist(args[1]) ? getClient(args[1]) : getClient(getPlayer(args[1]));
        }
        if (!sender.hasPermission("dglab.info.others") && !Objects.equals(sender, client.getPlayer())) throw new CmdException("你没有权限查询其他玩家");
    }

    @Override
    protected void run() {
        sender.sendMessage(client.info());
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return getPlayerAndClientList(sender);
        return null;
    }
}

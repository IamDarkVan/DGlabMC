package darkvan.dglabmc.command.cmds;


import darkvan.dglabmc.Client;
import darkvan.dglabmc.command.CmdException;
import darkvan.dglabmc.utils.ClientUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static darkvan.dglabmc.utils.ClientUtils.getClient;
import static darkvan.dglabmc.utils.ClientUtils.isClientExist;
import static darkvan.dglabmc.utils.DGlabUtils.playerAndClients;
import static org.bukkit.Bukkit.getPlayer;

public class CommandInfo extends Command{


    public CommandInfo(@NotNull CommandSender sender, @NotNull String[] args) {
        super("info", sender, args, null, 2, "/dglab info [clientId|player] 查询app信息","dglab.info");
    }
    private Client client;

    @Override
    protected void errorHandle() throws CmdException {
        if (length == 1){
            if (!(sender instanceof Player player)) throw new CmdException("服务器后台查询绑定玩家请使用 /dglab info <clientId|player>");
            this.client = getClient(player);
        }
        if (length == 2){
            if (!ClientUtils.isClientExist(args[1]) && isClientExist(getPlayer(args[1]))) throw new CmdException("客户端不存在或玩家未绑定");
            this.client = ClientUtils.isClientExist(args[1]) ? ClientUtils.getClient(args[1]) : getClient(getPlayer(args[1]));
        }
        if (!sender.hasPermission("dglab.info.others") && Objects.equals(sender, client.getPlayer())) throw new CmdException("你没有权限查询其他玩家");
    }
    @Override
    protected void run() {
        sender.sendMessage(client.info());
    }
    @Override
    public List<String> tabComplete() {
        if (length == 2) return playerAndClients();
        return null;
    }
}

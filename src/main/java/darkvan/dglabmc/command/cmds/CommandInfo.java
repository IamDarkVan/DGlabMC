package darkvan.dglabmc.command.cmds;


import darkvan.dglabmc.Client;
import darkvan.dglabmc.command.CmdException;
import darkvan.dglabmc.utils.ClientUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static darkvan.dglabmc.utils.ClientUtils.*;
import static darkvan.dglabmc.utils.DGlabUtils.playerAndClients;
import static org.bukkit.Bukkit.getPlayer;

public class CommandInfo extends Command{


    public CommandInfo(@NotNull CommandSender sender, @NotNull String[] args, @Nullable String perm) {
        super(sender, args, null, 2, "/dglab info [clientId|player] 查询app信息", perm);
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

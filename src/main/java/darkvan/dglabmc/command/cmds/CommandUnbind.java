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

public class CommandUnbind extends Command{

    public CommandUnbind(@NotNull CommandSender sender, @NotNull String[] args, @Nullable String perm) {
        super("unbind", sender, args, 1, 2, "/dglab unbind [clientId|player] --解除玩家绑定app", perm);
    }

    Client client;
    @Override
    protected void errorHandle() throws CmdException {
        if (length == 1){
            if (!(sender instanceof Player player)) throw new CmdException("服务器后台请使用 /dglab unbind <clientId|player>");
            if (!isClientExist(player)) throw new CmdException("你还没有绑定的app");
        }
        if (length == 2){
            if (!ClientUtils.isClientExist(args[1]) && !isClientExist(getPlayer(args[1]))) throw new CmdException("客户端不存在或玩家未绑定");
            this.client = ClientUtils.isClientExist(args[1]) ? ClientUtils.getClient(args[1]) : getClient(getPlayer(args[1]));
            if (client.getPlayer() == null) throw new CmdException("app还未被绑定");
        }
    }

    @Override
    protected void run() {
        client.bind(null);
        sender.sendMessage("成功解绑: " + client.getClientId());
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return playerAndClients();
        return null;
    }
}

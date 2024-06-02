package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.Client;
import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static darkvan.dglabmc.utils.ClientUtils.getClient;
import static darkvan.dglabmc.utils.ClientUtils.isClientExist;
import static darkvan.dglabmc.utils.CommandUtils.getClientList;
import static darkvan.dglabmc.utils.CommandUtils.getPlayerList;
import static org.bukkit.Bukkit.getPlayer;

public class CommandBind extends Command {
    private Client client;
    private Player player;
    public CommandBind(@NotNull CommandSender sender, @NotNull String[] args) {
        super("bind", sender, args, 2, 3, "/dglab bind <clientId> [player] -- 玩家绑定app 使用ctrl-指令不需要clientId", "dglab.bind");
    }

    @Override
    protected void errorHandle() throws CmdException {
        if (length == 2) {
            if (!(sender instanceof Player)) throw new CmdException("服务器后台绑定玩家请使用 /dglab bind <player> <clientId>");
            this.player = (Player) sender;
        }
        if (length == 3) {
            if (getPlayer(args[2]) == null) throw new CmdException("玩家不存在");
            this.player = getPlayer(args[2]);
        }
        if (!isClientExist(args[1])) throw new CmdException("客户端不存在");
        this.client = getClient(args[1]);
        if (client.getPlayer() != null) throw new CmdException("你要绑定的客户端已被绑定");
        if (!sender.hasPermission("dglab.bind.others") && Objects.equals(player, sender)) throw new CmdException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        client.bind(player);
        sender.sendMessage("成功绑定: " + player.getName() + " <-> " + args[1]);
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return getClientList(sender);
        if (length == 3) return getPlayerList(sender);
        return null;
    }
}

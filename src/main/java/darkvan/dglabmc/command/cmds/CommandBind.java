package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.Client;
import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static darkvan.dglabmc.DGlabMC.clients;
import static darkvan.dglabmc.utils.ClientUtils.getClientById;
import static darkvan.dglabmc.utils.ClientUtils.isClientIdExist;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.Bukkit.getPlayer;

public class CommandBind extends Command{
    public CommandBind(@NotNull CommandSender sender, @NotNull String[] args, @Nullable String perm) {
        super(sender, args, 2, 3, "/dglab bind <clientId> [player] -- 玩家绑定app 使用ctrl-指令不需要clientId", perm);
    }
    Client client;
    @Override
    protected void errorHandle() throws CmdException{
        if(length == 2 && !(sender instanceof Player)) throw new CmdException("服务器后台绑定玩家请使用 /dglab bind <player> <clientId>");
        if (length == 3 && getPlayer(args[2]) == null) throw new CmdException("玩家不存在");
        if (!isClientIdExist(args[1])) throw new CmdException("客户端不存在");
        this.client = getClientById(args[1]);
        if (client.getPlayer() != null) throw new CmdException("你要绑定的客户端已被绑定且服务器不允许多绑定");
    }
    @Override
    protected void run() {
        if (length == 2){
            Player player = (Player) sender;
            client.bind(player);
            sender.sendMessage("成功绑定: " + player.getName() + " <-> " + args[1]);
        }
        if (length == 3){
            client.bind(getPlayer(args[2]));
            sender.sendMessage("成功绑定: " + args[2] + " <-> " + args[1]);
        }
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return clients.stream().map(Client::getClientId).toList();
        if (length == 3) return getOnlinePlayers().stream().map(Player::getName).toList();
        return null;
    }
}

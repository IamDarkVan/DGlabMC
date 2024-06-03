package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.Client;
import darkvan.dglabmc.command.CmdException;
import darkvan.dglabmc.games.Game;
import darkvan.dglabmc.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static darkvan.dglabmc.games.Game.games;
import static darkvan.dglabmc.games.Game.getGame;
import static darkvan.dglabmc.utils.ClientUtils.getClient;
import static darkvan.dglabmc.utils.ClientUtils.isClientExist;
import static darkvan.dglabmc.utils.CommandUtils.concatList;
import static org.bukkit.Bukkit.getPlayer;

public class CommandGame extends Command {

    private Client client;
    private Game game;
    private String type;
    public CommandGame(@NotNull CommandSender sender, @Nullable String[] args) {
        super("game", sender, args, 3, 4, "/dglab game [clientId|player] <game> (enable|disable|toggle) -- 为客户端启用/禁用游戏", "dglab.game");
    }

    @Override
    protected void errorHandle() throws CmdException {
        if (length == 3) {
            if (!(sender instanceof Player)) throw new CmdException("服务器后台请使用 /dglab game <clientId|player> <game> (enable|disable|toggle)");
            Player player = (Player) sender;
            if (!isClientExist(player)) throw new CmdException("你还没有绑定的app");
            if (getGame(args[1]) == null) throw new CmdException("未找到该游戏");
            this.client = getClient(player);
            this.game = getGame(args[1]);
            this.type = args[2];
        }
        if (length == 4) {
            if (!isClientExist(args[1]) && !isClientExist(getPlayer(args[1]))) throw new CmdException("客户端不存在或玩家未绑定");
            if (getGame(args[2]) == null) throw new CmdException("未找到该游戏");
            this.client = isClientExist(args[1]) ? getClient(args[1]) : getClient(getPlayer(args[1]));
            this.game = getGame(args[2]);
            this.type = args[3];
        }
        if (!Arrays.asList("enable", "disable", "toggle").contains(type)) throw new CmdException("请输入 enable|disable|toggle");
        if (!sender.hasPermission("dglab.game.others") && !Objects.equals(sender, client.getPlayer())) throw new CmdException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        if ("enable".equals(type)) game.enableClient(client);
        if ("disable".equals(type)) game.disableClient(client);
        if ("toggle".equals(type)) game.toggleClient(client);
        sender.sendMessage("成功为" + client.getClientId() + (game.isClientEnabled(client) ? "启用" : "取消") + "游戏");
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return concatList(CommandUtils.getPlayerAndClientList(sender), games.keySet().toArray(new String[0]));
        if (getPlayer(args[1]) != null || isClientExist(args[1])) {
            if (length == 3) return new ArrayList<>(games.keySet());
            if (length == 4) return Arrays.asList("enable", "disable", "toggle");
        } else {
            if (length == 3) return Arrays.asList("enable", "disable", "toggle");
        }
        return null;
    }
}

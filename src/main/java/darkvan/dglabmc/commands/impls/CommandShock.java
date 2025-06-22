package darkvan.dglabmc.commands.impls;

import darkvan.dglabmc.Client;
import darkvan.dglabmc.commands.CommandException;
import darkvan.dglabmc.commands.CommandAbstract;
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

public class CommandShock extends CommandAbstract {
    private Client client;
    private Integer second;
    private boolean replace;

    public CommandShock(@NotNull CommandSender sender, @Nullable String[] args) {
        super("shock", sender, args, 2, 3, "/dglab shock [clientId|player] <time(sec)> -- 放电,时间正加负减,无符号为重置,0停止", "dglab.shock");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (length == 2) {
            if (!(sender instanceof Player)) throw new CommandException("服务器后台请使用 /dglab shock <clientId|player> <time(sec)>");
            Player player = (Player) sender;
            if (!isClientExist(player)) throw new CommandException("你还没有绑定的app");
            if (!args[1].matches("^[+-]?\\d+$")) throw new CommandException("时间(秒)必须为不含小数的纯数字");
            this.client = getClient(player);
            this.second = Integer.parseInt(args[1]);
            this.replace = !args[1].matches("^[+-].*");
        }
        if (length == 3) {
            if (!isClientExist(args[1]) && !isClientExist(getPlayer(args[1]))) throw new CommandException("客户端不存在或玩家未绑定");
            if (!args[2].matches("^[+-]?\\d+$")) throw new CommandException("时间(秒)必须为不含小数的纯数字");
            this.client = isClientExist(args[1]) ? getClient(args[1]) : getClient(getPlayer(args[1]));
            this.second = Integer.parseInt(args[2]);
            this.replace = !args[2].matches("^[+-].*");
        }
        if (client.getAPulse() == null && client.getBPulse() == null) throw new CommandException("频道A,B中必须有至少一个设置了波形");
        if (!sender.hasPermission("dglab.shock.others") && !Objects.equals(sender, client.getPlayer())) throw new CommandException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        client.giveShock(second, replace);
        if (replace) {
            sender.sendMessage("电击时间设为" + second + "秒");
            return;
        }
        if (second > 0) sender.sendMessage("电击时间+" + second + "秒");
        if (second < 0) sender.sendMessage("电击时间" + second + "秒");
        if (second == 0) sender.sendMessage("成功停止电击");
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return getPlayerAndClientList(sender);
        return null;
    }
}

package shirohaNya.dglabmc.commands.impls;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.Client;
import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.enums.Channel;
import shirohaNya.dglabmc.utils.CommandUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;
import static shirohaNya.dglabmc.utils.ClientUtils.getClient;
import static shirohaNya.dglabmc.utils.ClientUtils.isClientExist;
import static shirohaNya.dglabmc.utils.CommandUtils.concatList;

public class CommandShock extends CommandAbstract {
    private Client client;
    private Integer second;
    private boolean replace;
    private String channel;
    private Channel _channel;

    public CommandShock(@NotNull CommandSender sender, @Nullable String[] args) {
        super("shock", sender, args, 3, 4, "/dglab shock [clientId|player] <A|B|both> <time(sec)> -- 放电,时间正加负减,无符号为重置,0停止", "dglab.shock");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (length == 3) {
            if (!(sender instanceof Player))
                throw new CommandException("服务器后台请使用 /dglab shock <clientId|player> <A|B|both> <time(sec)>");
            Player player = (Player) sender;
            if (!isClientExist(player)) throw new CommandException("你还没有绑定的app");
            if (!args[2].matches("^[+-]?\\d+$")) throw new CommandException("时间(秒)必须为不含小数的纯数字");
            this.client = getClient(player);
            this.second = Integer.parseInt(args[1]);
            this.replace = !args[2].matches("^[+-].*");
            this.channel = args[1];
        }
        if (length == 4) {
            if (!isClientExist(args[1]) && !isClientExist(getPlayer(args[1])))
                throw new CommandException("客户端不存在或玩家未绑定");
            if (!args[3].matches("^[+-]?\\d+$")) throw new CommandException("时间(秒)必须为不含小数的纯数字");
            this.client = isClientExist(args[1]) ? getClient(args[1]) : getClient(getPlayer(args[1]));
            this.second = Integer.parseInt(args[3]);
            this.replace = !args[3].matches("^[+-].*");
            this.channel = args[2];
        }
        try {
            this._channel = Channel.toChannel(channel);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e);
        }
        if (client.getAPulse() == null && client.getBPulse() == null)
            throw new CommandException("频道A,B中必须有至少一个设置了波形");
        if (!sender.hasPermission("dglab.shock.others") && !Objects.equals(sender, client.getPlayer()))
            throw new CommandException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        client.giveShock(second, _channel, replace);
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
        if (length == 2) return concatList(CommandUtils.getPlayerAndClientList(sender), "A", "B", "both");
        if (getPlayer(args[1]) != null || isClientExist(args[1])) {
            if (length == 3) return Arrays.asList("A", "B", "both");
        }
        return null;
    }
}

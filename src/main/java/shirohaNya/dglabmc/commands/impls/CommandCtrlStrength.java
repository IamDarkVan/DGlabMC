package shirohaNya.dglabmc.commands.impls;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.Client;
import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.enums.AdjustMode;
import shirohaNya.dglabmc.enums.Channel;
import shirohaNya.dglabmc.utils.CommandUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;
import static shirohaNya.dglabmc.utils.ClientUtils.getClient;
import static shirohaNya.dglabmc.utils.ClientUtils.isClientExist;
import static shirohaNya.dglabmc.utils.CommandUtils.concatList;

public class CommandCtrlStrength extends CommandAbstract {
    private String channel, mode, value;
    private Channel _channel;
    private AdjustMode _mode;
    private Client client;

    public CommandCtrlStrength(@NotNull CommandSender sender, @Nullable String[] args) {
        super("ctrl-strength", sender, args, 4, 5,
                "/dglab ctrl-strength [player] (A|B|both) (add|dec|set) <value> -- 控制强度 (通道 模式 数值)",
                "dglab.ctrl.strength");
    }

    @Override
    protected void errorHandle() throws CommandException {
        Player player;
        if (length == 4) {
            if (!(sender instanceof Player))
                throw new CommandException("服务器后台请使用 /dglab ctrl-strength <player> (A|B|both) (add|dec|set) <value>");
            player = (Player) sender;
            if (!isClientExist(player)) throw new CommandException("你还没有绑定的app");
            this.client = getClient(player);
            this.channel = args[1];
            this.mode = args[2];
            this.value = args[3];
        }
        if (length == 5) {
            player = getPlayer(args[1]);
            if (isClientExist(player)) throw new CommandException("玩家未绑定");
            this.client = getClient(player);
            this.channel = args[2];
            this.mode = args[3];
            this.value = args[4];
        }
        try {
            this._channel = Channel.toChannel(channel);
            this._mode = AdjustMode.toMode(mode);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e);
        }
        if (!value.matches("\\d++")) throw new CommandException("数值请输入不含小数的纯数字");
        if (!sender.hasPermission("dglab.ctrl.others") && !Objects.equals(sender, client.getPlayer()))
            throw new CommandException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        client.adjustStrength(_channel, _mode, Integer.parseInt(value));
        sender.sendMessage("通道" + channel + "成功" + ("1".equals(mode) ? "增加" : "0".equals(mode) ? "减少" : "设置为") + value);
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return concatList(CommandUtils.getPlayerList(sender), "A", "B", "both");
        if (getPlayer(args[1]) != null) {
            if (length == 3) return Arrays.asList("A", "B", "both");
            if (length == 4) return Arrays.asList("add", "dec", "set");
        } else {
            if (length == 3) return Arrays.asList("add", "dec", "set");
        }
        return null;
    }
}

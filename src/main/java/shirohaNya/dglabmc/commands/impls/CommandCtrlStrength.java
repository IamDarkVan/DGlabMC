package shirohaNya.dglabmc.commands.impls;

import shirohaNya.dglabmc.Client;
import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static shirohaNya.dglabmc.DGlabMC.mcUUID;
import static shirohaNya.dglabmc.utils.ClientUtils.getClient;
import static shirohaNya.dglabmc.utils.ClientUtils.isClientExist;
import static shirohaNya.dglabmc.utils.CommandUtils.concatList;
import static shirohaNya.dglabmc.utils.DGlabUtils.toDGJson;
import static org.bukkit.Bukkit.getPlayer;

public class CommandCtrlStrength extends CommandAbstract {
    private String channel, mode, value;
    private Client client;
    public CommandCtrlStrength(@NotNull CommandSender sender, @Nullable String[] args) {
        super("ctrl-strength", sender, args, 4, 5,
                "/dglab ctrl-strength [clientId|player] (A|B|both) (add|dec|set) <value> -- 控制强度 (通道 模式 数值)",
                "dglab.ctrl.strength");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (length == 4) {
            if (!(sender instanceof Player)) throw new CommandException("服务器后台请使用 /dglab ctrl-strength <clientId|player> (A|B|both) (add|dec|set) <value>");
            Player player = (Player) sender;
            if (!isClientExist(player)) throw new CommandException("你还没有绑定的app");
            this.client = getClient(player);
            this.channel = args[1];
            this.mode = args[2];
            this.value = args[3];
        }
        if (length == 5) {
            if (!isClientExist(args[1]) && isClientExist(getPlayer(args[1]))) throw new CommandException("客户端不存在或玩家未绑定");
            this.client = isClientExist(args[1]) ? getClient(args[1]) : getClient(getPlayer(args[1]));
            this.channel = args[2];
            this.mode = args[3];
            this.value = args[4];
        }
        if (!Arrays.asList("a", "b", "both").contains(channel)) throw new CommandException("频道请输入 A B both 其中一个");
        if (!Arrays.asList("add", "dec", "set").contains(mode)) throw new CommandException("模式请输入 add dec set 其中一个");
        if (!value.matches("\\d++")) throw new CommandException("数值请输入不含小数的纯数字");
        if (!sender.hasPermission("dglab.ctrl.others") && !Objects.equals(sender, client.getPlayer())) throw new CommandException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        this.mode = "add".equals(mode) ? "1" : "dec".equals(mode) ? "0" : "2";
        if ("a".equals(channel) || "both".equals(channel))
            client.output(toDGJson("msg", mcUUID, client.getClientId(), "strength-1+" + mode + "+" + value));
        if ("b".equals(channel) || "both".equals(channel))
            client.output(toDGJson("msg", mcUUID, client.getClientId(), "strength-2+" + mode + "+" + value));
        sender.sendMessage("通道" + channel + "成功" + ("1".equals(mode) ? "增加" : "0".equals(mode) ? "减少" : "设置为") + value);
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return concatList(CommandUtils.getPlayerAndClientList(sender), "A", "B", "both");
        if (getPlayer(args[1]) != null || isClientExist(args[1])) {
            if (length == 3) return Arrays.asList("A", "B", "both");
            if (length == 4) return Arrays.asList("add", "dec", "set");
        } else {
            if (length == 3) return Arrays.asList("add", "dec", "set");
        }
        return null;
    }
}

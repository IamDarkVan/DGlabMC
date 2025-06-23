package shirohaNya.dglabmc.commands.impls;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.Client;
import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.enums.Channel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;
import static shirohaNya.dglabmc.utils.ClientUtils.getClient;
import static shirohaNya.dglabmc.utils.ClientUtils.isClientExist;
import static shirohaNya.dglabmc.utils.CommandUtils.concatList;
import static shirohaNya.dglabmc.utils.CommandUtils.getPlayerAndClientList;

public class CommandCtrlPulse extends CommandAbstract {
    private String channel, hex;
    private Channel _channel;
    private Client client;
    public CommandCtrlPulse(@NotNull CommandSender sender, @Nullable String[] args) {
        super("ctrl-pulse", sender, args, 3, 4,
                "/dglab ctrl-pulse [clientId|player] (A|B|both) (<HEX[]>|clear) -- 控制波形 例:[xxxxxxxxxxxxxxxx,xxxxxxxxxxxxxxxx,......,xxxxxxxxxxxxxxxx]",
                "dglab.ctrl.pulse");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (length == 3) {
            if (!(sender instanceof Player)) throw new CommandException("服务器后台请使用 /dglab ctrl-pulse <clientId|player> (A|B|both) (<HEX[]>|clear)");
            Player player = (Player) sender;
            if (!isClientExist(player)) throw new CommandException("你还没有绑定的app");
            this.client = getClient(player);
            this.channel = args[1];
            this.hex = args[2].toUpperCase();
        }
        if (length == 4) {
            if (!isClientExist(args[1]) && !isClientExist(getPlayer(args[1]))) throw new CommandException("客户端不存在或玩家未绑定");
            this.client = isClientExist(args[1]) ? getClient(args[1]) : getClient(getPlayer(args[1]));
            this.channel = args[2];
            this.hex = args[3].toUpperCase();
        }
        try {
            this._channel = Channel.toChannel(channel);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e);
        }
        if ("CLEAR".equals(hex) && hex.matches("^\\[[0-9A-F,]*]$")) throw new CommandException("hex数组不符合规范");
        if (!sender.hasPermission("dglab.ctrl.others") && !Objects.equals(sender, client.getPlayer())) throw new CommandException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        if ("CLEAR".equals(hex)) this.hex = null;
        client.adjustPulse(_channel, hex);
        sender.sendMessage("通道 " + channel + " 成功设置波形为 " + hex);
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return concatList(getPlayerAndClientList(sender), "A", "B", "both");
        if (getPlayer(args[1]) != null || isClientExist(args[1])) {
            if (length == 3) return Arrays.asList("A", "B", "both");
            if (length == 4) return Collections.singletonList("clear");
        } else {
            if (length == 3) return Collections.singletonList("clear");
        }
        return null;
    }
}

package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.Client;
import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static darkvan.dglabmc.DGlabMC.mcUUID;
import static darkvan.dglabmc.utils.ClientUtils.getClient;
import static darkvan.dglabmc.utils.ClientUtils.isClientExist;
import static darkvan.dglabmc.utils.CommandUtils.concatList;
import static darkvan.dglabmc.utils.CommandUtils.getPlayerAndClientList;
import static darkvan.dglabmc.utils.DGlabUtils.toDGJson;
import static org.bukkit.Bukkit.getPlayer;

public class CommandCtrlPulse extends Command {
    private String channel, hex;
    private Client client;
    public CommandCtrlPulse(@NotNull CommandSender sender, @NotNull String[] args) {
        super("ctrl-pulse", sender, args, 3, 4,
                "/dglab ctrl-pulse [clientId|player] (A|B|both) (<HEX[]>|clear) -- 控制波形 例:[xxxxxxxxxxxxxxxx,xxxxxxxxxxxxxxxx,......,xxxxxxxxxxxxxxxx]",
                "dglab.ctrl.pulse");
    }

    @Override
    protected void errorHandle() throws CmdException {
        if (length == 3) {
            if (!(sender instanceof Player)) throw new CmdException("服务器后台请使用 /dglab ctrl-pulse <clientId|player> (A|B|both) (<HEX[]>|clear)");
            Player player = (Player) sender;
            if (!isClientExist(player)) throw new CmdException("你还没有绑定的app");
            this.client = getClient(player);
            this.channel = args[1];
            this.hex = args[2].toUpperCase();
        }
        if (length == 4) {
            if (!isClientExist(args[1]) && !isClientExist(getPlayer(args[1]))) throw new CmdException("客户端不存在或玩家未绑定");
            this.client = isClientExist(args[1]) ? getClient(args[1]) : getClient(getPlayer(args[1]));
            this.channel = args[2];
            this.hex = args[3].toUpperCase();
        }
        if (!Arrays.asList("a", "b", "both").contains(channel)) throw new CmdException("频道请输入 A B both 其中一个");
        if ("CLEAR".equals(hex) && hex.matches("^\\[[0-9A-F,]*]$")) throw new CmdException("hex数组不符合规范");
        if (!sender.hasPermission("dglab.ctrl.others") && Objects.equals(sender, client.getPlayer())) throw new CmdException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        if ("CLEAR".equals(hex)) {
            if ("a".equals(channel) || "both".equals(channel)) client.output(toDGJson("msg", mcUUID, client.getClientId(), "clear-1"));
            if ("b".equals(channel) || "both".equals(channel)) client.output(toDGJson("msg", mcUUID, client.getClientId(), "clear-2"));
            return;
        }
        if ("a".equals(channel) || "both".equals(channel)) client.setAPulse(hex);
        if ("b".equals(channel) || "both".equals(channel)) client.setAPulse(hex);
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

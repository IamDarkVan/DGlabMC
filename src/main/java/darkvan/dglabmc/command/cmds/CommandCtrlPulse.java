package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.Client;
import darkvan.dglabmc.command.CmdException;
import darkvan.dglabmc.utils.ClientUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static darkvan.dglabmc.DGlabMC.mcUUID;
import static darkvan.dglabmc.utils.ClientUtils.*;
import static darkvan.dglabmc.utils.DGlabUtils.playerAndClients;
import static darkvan.dglabmc.utils.DGlabUtils.toDGJson;
import static org.bukkit.Bukkit.getPlayer;

public class CommandCtrlPulse extends Command{
    public CommandCtrlPulse(@NotNull CommandSender sender, @NotNull String[] args, @Nullable String perm) {
        super("ctrl-pulse", sender, args, 3, 4, "/dglab ctrl-pulse [clientId|player] (A|B|both) (<HEX[]>|clear) -- 控制波形 例:[xxxxxxxxxxxxxxxx,xxxxxxxxxxxxxxxx,......,xxxxxxxxxxxxxxxx]", perm);
    }

    String channel, hex;
    Client client;
    @Override
    protected void errorHandle() throws CmdException {
        if (length == 3) {
            if (!(sender instanceof Player player)) throw new CmdException("服务器后台请使用 /dglab ctrl-pulse [clientId|player] (A|B|both) (<HEX[]>|clear)");
            if (!isClientExist(player)) throw new CmdException("你还没有绑定的app");
            this.client = getClient(player);
            this.channel = args[1];
            this.hex = args[2].toUpperCase();
        }
        if(length == 4){
            if (!ClientUtils.isClientExist(args[1]) && !isClientExist(getPlayer(args[1]))) throw new CmdException("客户端不存在或玩家未绑定");
            this.client = ClientUtils.isClientExist(args[1]) ? ClientUtils.getClient(args[1]) : getClient(getPlayer(args[1]));
            this.channel = args[2];
            this.hex = args[3].toUpperCase();
        }
        if (!Arrays.asList("a", "b", "both").contains(channel)) throw new CmdException("频道请输入 A B both 其中一个");
        if ("CLEAR".equals(hex) && hex.matches("^\\[[0-9A-F,]*]$")) throw new CmdException("hex数组不符合规范");
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
        if (length == 2) return Stream.concat(Stream.of("A", "B", "both"),playerAndClients().stream()).toList();
        if (getPlayer(args[1]) != null || ClientUtils.isClientExist(args[1])) {
            if (length == 3) return Arrays.asList("A", "B", "both");
            if (length == 4) return List.of("clear");
        } else {
            if (length == 3) return List.of("clear");
        }
        return null;
    }
}

package shirohaNya.dglabmc.commands.impls;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.api.Client;
import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.commands.CommandException;

import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;
import static shirohaNya.dglabmc.client.ClientManager.getClient;
import static shirohaNya.dglabmc.client.ClientManager.isClientExist;
import static shirohaNya.dglabmc.utils.CommandUtils.getPlayerList;

public class CommandInfo extends CommandAbstract {


    private Client client;

    public CommandInfo(@NotNull CommandSender sender, @Nullable String[] args) {
        super("info", sender, args, null, 2, "/dglab info [player] 查询app信息", "dglab.info");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (length == 1) {
            if (!(sender instanceof Player))
                throw new CommandException("服务器后台请使用 /dglab info <player>");
            Player player = (Player) sender;
            if (!isClientExist(player)) throw new CommandException("你还没有绑定的app");
            this.client = getClient(player);
        }
        if (length == 2) {
            if (!isClientExist(getPlayer(args[1]))) throw new CommandException("客户端不存在或玩家未绑定");
            this.client = getClient(getPlayer(args[1]));
        }
        if (!sender.hasPermission("dglab.info.others") && !Objects.equals(sender, client.getPlayer()))
            throw new CommandException("你没有权限查询其他玩家");
    }

    @Override
    protected void run() {
        sender.sendMessage("========== 绑定信息 ==========");
        sender.sendMessage(client.getTargetId() + " " + (client.getPlayer() == null ? "未绑定" : client.getPlayer().getName()));
        sender.sendMessage("A通道强度:" + client.getAStrength() + "/" + client.getAMaxStrength() +
                "电击剩余时间:" + (client.getATotalTime() - client.getATicks() / 20) + "秒");
        sender.sendMessage("B通道强度:" + client.getBStrength() + "/" + client.getBMaxStrength() +
                "电击剩余时间:" + (client.getBTotalTime() - client.getBTicks() / 20) + "秒");
        sender.sendMessage("已启用的脚本: ");
        client.getEnabledScripts().forEach(script -> sender.sendMessage(script.getName()));
        sender.sendMessage("=============================");
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return getPlayerList(sender);
        return null;
    }
}

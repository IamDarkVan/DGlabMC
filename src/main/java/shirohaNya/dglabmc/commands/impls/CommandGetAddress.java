package shirohaNya.dglabmc.commands.impls;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.commands.CommandException;

import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;
import static shirohaNya.dglabmc.utils.CommandUtils.getPlayerList;
import static shirohaNya.dglabmc.utils.DGlabUtils.getPlayerUrl;

public class CommandGetAddress extends CommandAbstract {
    private Player bindPlayer;
    public CommandGetAddress(@NotNull CommandSender sender, @Nullable String[] args) {
        super("getaddress", sender, args, 1, 2, "/dglab getAddress [player] -- 获取二维码的地址", "dglab.getAddress");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (!(sender instanceof Player)) throw new CommandException("服务器后台请查看插件配置文件夹");
        Player player = (Player) sender;
        if (length == 1) {
            this.bindPlayer = player;
        }
        if (length == 2) {
            this.bindPlayer = getPlayer(args[1]);
            if (bindPlayer == null) throw new CommandException("玩家不存在");
        }
        if (!sender.hasPermission("dglab.bind.others") && !Objects.equals(bindPlayer, sender))
            throw new CommandException("你没有权限查询其他玩家");
    }

    @Override
    protected void run() {
        String url = getPlayerUrl(bindPlayer);
        sender.sendMessage(url);
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return getPlayerList(sender);
        return null;
    }
}

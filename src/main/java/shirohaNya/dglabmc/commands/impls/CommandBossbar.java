package shirohaNya.dglabmc.commands.impls;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.Client;
import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.enums.BossbarType;
import shirohaNya.dglabmc.utils.CommandUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;
import static shirohaNya.dglabmc.utils.ClientUtils.getClient;
import static shirohaNya.dglabmc.utils.ClientUtils.isClientExist;
import static shirohaNya.dglabmc.utils.CommandUtils.concatList;

public class CommandBossbar extends CommandAbstract {
    private Client client;
    private String type;
    private BossbarType _type;

    public CommandBossbar(@NotNull CommandSender sender, @Nullable String[] args) {
        super("bossbar", sender, args, 2, 3, "/dglab bossbar [clientId|player] <none|A|B|both> -- 更改电击提示", "dglab.bossbar");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (length == 2) {
            if (!(sender instanceof Player))
                throw new CommandException("服务器后台请用/dglab bossbar <clientId|player> <none|A|B|both>");
            Player player = (Player) sender;
            if (!isClientExist(player)) throw new CommandException("你还没有绑定的app");
            this.client = getClient(player);
            this.type = args[1];
        }
        if (length == 3) {
            if (!isClientExist(args[1]) && !isClientExist(getPlayer(args[1])))
                throw new CommandException("客户端不存在或玩家未绑定");
            this.client = isClientExist(args[1]) ? getClient(args[1]) : getClient(getPlayer(args[1]));
            this.type = args[2];
        }
        try {
            this._type = BossbarType.toType(type);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e);
        }
        if (!sender.hasPermission("dglab.ctrl.others") && !Objects.equals(sender, client.getPlayer()))
            throw new CommandException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        client.getBossbar().setBossbarType(_type);
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return concatList(CommandUtils.getPlayerAndClientList(sender), "none", "A", "B", "both");
        if (length == 3) return Arrays.asList("none", "A", "B", "both");
        return null;
    }
}

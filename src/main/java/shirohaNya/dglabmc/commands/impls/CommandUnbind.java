package shirohaNya.dglabmc.commands.impls;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.Client;
import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.commands.CommandException;

import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;
import static shirohaNya.dglabmc.utils.ClientUtils.getClient;
import static shirohaNya.dglabmc.utils.ClientUtils.isClientExist;
import static shirohaNya.dglabmc.utils.CommandUtils.getPlayerList;

public class CommandUnbind extends CommandAbstract {

    private Client client;

    public CommandUnbind(@NotNull CommandSender sender, @Nullable String[] args) {
        super("unbind", sender, args, 1, 2, "/dglab unbind [clientId|player] --解除玩家绑定app", "dglab.bind");
    }

    @Override
    protected void errorHandle() throws CommandException {
        Player player;
        if (length == 1) {
            if (!(sender instanceof Player))
                throw new CommandException("服务器后台请使用 /dglab ctrl-pulse [clientId|player] (A|B|both) (<HEX[]>|clear)");
            player = (Player) sender;
            if (!isClientExist(player)) throw new CommandException("你还没有绑定的app");
            this.client = getClient(player);
        }
        if (length == 2) {
            if (!isClientExist(getPlayer(args[1])))
                throw new CommandException("客户端不存在或玩家未绑定");
            player = getPlayer(args[1]);
            this.client = getClient(player);
        }
        if (!sender.hasPermission("dglab.bind.others") && !Objects.equals(sender, client.getPlayer()))
            throw new CommandException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        client.unbind();
        sender.sendMessage("成功解绑: " + client.getTargetId());
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return getPlayerList(sender);
        return null;
    }
}

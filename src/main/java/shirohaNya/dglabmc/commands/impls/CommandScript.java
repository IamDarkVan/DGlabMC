package shirohaNya.dglabmc.commands.impls;

import shirohaNya.dglabmc.Client;
import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.scripts.Script;
import shirohaNya.dglabmc.scripts.ScriptManager;
import shirohaNya.dglabmc.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static shirohaNya.dglabmc.scripts.ScriptManager.getScript;
import static shirohaNya.dglabmc.utils.ClientUtils.getClient;
import static shirohaNya.dglabmc.utils.ClientUtils.isClientExist;
import static shirohaNya.dglabmc.utils.CommandUtils.concatList;
import static org.bukkit.Bukkit.getPlayer;

public class CommandScript extends CommandAbstract {

    private Client client;
    private Script script;
    private String type;
    public CommandScript(@NotNull CommandSender sender, @Nullable String[] args) {
        super("script", sender, args, 3, 4, "/dglab script [clientId|player] <script> (enable|disable|toggle) -- 为客户端启用/禁用脚本", "dglab.script");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (length == 3) {
            if (!(sender instanceof Player)) throw new CommandException("服务器后台请使用 /dglab script <clientId|player> <script> (enable|disable|toggle)");
            Player player = (Player) sender;
            if (!isClientExist(player)) throw new CommandException("你还没有绑定的app");
            if (getScript(args[1]) == null) throw new CommandException("未找到该游戏");
            this.client = getClient(player);
            this.script = getScript(args[1]);
            this.type = args[2];
        }
        if (length == 4) {
            if (!isClientExist(args[1]) && !isClientExist(getPlayer(args[1]))) throw new CommandException("客户端不存在或玩家未绑定");
            if (getScript(args[2]) == null) throw new CommandException("未找到该游戏");
            this.client = isClientExist(args[1]) ? getClient(args[1]) : getClient(getPlayer(args[1]));
            this.script = getScript(args[2]);
            this.type = args[3];
        }
        if (!Arrays.asList("enable", "disable", "toggle").contains(type)) throw new CommandException("请输入 enable|disable|toggle");
        if (!sender.hasPermission("dglab.script.others") && !Objects.equals(sender, client.getPlayer())) throw new CommandException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        if ("enable".equals(type)) script.enableClient(client);
        if ("disable".equals(type)) script.disableClient(client);
        if ("toggle".equals(type)) script.toggleClient(client);
        sender.sendMessage("成功为" + client.getClientId() + (script.isClientEnabled(client) ? "启用" : "取消") + "游戏");
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return concatList(CommandUtils.getPlayerAndClientList(sender), ScriptManager.getScriptMap().keySet().toArray(new String[0]));
        if (getPlayer(args[1]) != null || isClientExist(args[1])) {
            if (length == 3) return new ArrayList<>(ScriptManager.getScriptMap().keySet());
            if (length == 4) return Arrays.asList("enable", "disable", "toggle");
        } else {
            if (length == 3) return Arrays.asList("enable", "disable", "toggle");
        }
        return null;
    }
}

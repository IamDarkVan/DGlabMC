package shirohaNya.dglabmc.commands.impls;

import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.scripts.ScriptManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class CommandScriptList extends CommandAbstract {

    public CommandScriptList(@NotNull CommandSender sender, @Nullable String[] args) {
        super("script-list", sender, args, null, null, "/dglab script-list 查看脚本列表", "dglab.script.list");
    }

    @Override
    protected void errorHandle() throws CommandException {

    }

    @Override
    protected void run() {
        sender.sendMessage("========== 脚本列表 ==========");
        ScriptManager.getScriptSet().forEach(script -> sender.sendMessage(script.getName() + " " + script.getDescription()));
        sender.sendMessage("=============================");
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

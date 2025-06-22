package darkvan.dglabmc.commands.impls;

import darkvan.dglabmc.commands.CommandAbstract;
import darkvan.dglabmc.commands.CommandException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static darkvan.dglabmc.DGlabMC.scripts;

public class CommandScriptList extends CommandAbstract {

    public CommandScriptList(@NotNull CommandSender sender, @Nullable String[] args) {
        super("script-list", sender, args, null, null, "/dglab script-list 查看脚本列表", "dglab.script.list");
    }

    @Override
    protected void errorHandle() throws CommandException {

    }

    @Override
    protected void run() {
        sender.sendMessage("以下是脚本列表:");
        sender.sendMessage("====================");
        scripts.values().forEach(script -> sender.sendMessage(script.getName() + " " + script.getDescription()));
        sender.sendMessage("====================");
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

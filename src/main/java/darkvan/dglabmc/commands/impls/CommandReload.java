package darkvan.dglabmc.commands.impls;

import darkvan.dglabmc.commands.CommandException;
import darkvan.dglabmc.commands.CommandAbstract;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static darkvan.dglabmc.utils.DGlabUtils.reloadConfigFile;

public class CommandReload extends CommandAbstract {

    public CommandReload(@NotNull CommandSender sender, @Nullable String[] args) {
        super("reload", sender, args, null, null, "/dglab reload -- 重载配置文件", "dglab.reload");
    }

    @Override
    protected void errorHandle() throws CommandException {
    }

    @Override
    protected void run() {
        reloadConfigFile();
        sender.sendMessage("成功重载Config文件");
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

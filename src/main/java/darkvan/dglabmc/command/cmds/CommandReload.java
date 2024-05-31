package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static darkvan.dglabmc.utils.DGlabUtils.reloadConfigFile;

public class CommandReload extends Command {

    public CommandReload(@NotNull CommandSender sender, @NotNull String[] args) {
        super("reload", sender, args, null, null, "/dglab reload -- 重载配置文件", "dglab.reload");
    }

    @Override
    protected void errorHandle() throws CmdException {
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

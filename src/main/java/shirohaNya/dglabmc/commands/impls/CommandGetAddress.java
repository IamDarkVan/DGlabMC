package shirohaNya.dglabmc.commands.impls;

import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.commands.CommandAbstract;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static shirohaNya.dglabmc.DGlabMC.plugin;

public class CommandGetAddress extends CommandAbstract {
    public CommandGetAddress(@NotNull CommandSender sender, @Nullable String[] args) {
        super("getaddress", sender, args, null, null, "/dglab getAddress -- 获取二维码的地址", "dglab.getAddress");
    }

    @Override
    protected void errorHandle() throws CommandException {
    }

    @Override
    protected void run() {
        sender.sendMessage(plugin.qrCode);
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

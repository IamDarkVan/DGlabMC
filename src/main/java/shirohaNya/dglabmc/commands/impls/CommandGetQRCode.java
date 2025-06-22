package shirohaNya.dglabmc.commands.impls;

import shirohaNya.dglabmc.commands.CommandException;
import shirohaNya.dglabmc.commands.CommandAbstract;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static shirohaNya.dglabmc.DGlabMC.plugin;

public class CommandGetQRCode extends CommandAbstract {
    public CommandGetQRCode(@NotNull CommandSender sender, @Nullable String[] args) {
        super("getqrcode", sender, args, null, null, "/dglab getQRCode -- 获取二维码", "dglab.getqrcode");
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

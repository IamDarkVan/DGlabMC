package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static darkvan.dglabmc.DGlabMC.plugin;

public class CommandGetQRCode extends Command{
    public CommandGetQRCode(@NotNull CommandSender sender, @NotNull String[] args, @Nullable String perm) {
        super("getqrcode", sender, args, null,null,"/dglab getQRCode -- 获取二维码", perm);
    }

    @Override
    protected void errorHandle() throws CmdException { }

    @Override
    protected void run() {
        sender.sendMessage(plugin.qrCode);
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

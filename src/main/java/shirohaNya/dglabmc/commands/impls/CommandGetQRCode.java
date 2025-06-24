package shirohaNya.dglabmc.commands.impls;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.commands.CommandException;

import java.awt.image.BufferedImage;
import java.util.List;

import static shirohaNya.dglabmc.DGlabMC.plugin;
import static shirohaNya.dglabmc.utils.DGlabUtils.generateQRCode;
import static shirohaNya.dglabmc.utils.DGlabUtils.giveMap;

public class CommandGetQRCode extends CommandAbstract {
    Player player;

    public CommandGetQRCode(@NotNull CommandSender sender, @Nullable String[] args) {
        super("getqrcode", sender, args, null, null, "/dglab getqrcode -- 获取二维码地图", "dglab.getQRcode");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (!(sender instanceof Player)) throw new CommandException("服务器后台请查看插件配置文件夹");
        this.player = (Player) sender;
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR)
            throw new CommandException("请空手执行该指令");
    }

    @Override
    protected void run() {
        try {
            BufferedImage img = generateQRCode(plugin.qrCode, 128);
            giveMap(player, img);
            sender.sendMessage("二维码生成成功,请查看背包");
        } catch (Exception e) {
            sender.sendMessage("二维码生成失败 未知错误");
            e.printStackTrace();
        }
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }

}

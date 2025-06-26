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
import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;
import static shirohaNya.dglabmc.utils.CommandUtils.getPlayerList;
import static shirohaNya.dglabmc.utils.DGlabUtils.*;

public class CommandBind extends CommandAbstract {
    private Player player, bindPlayer;

    public CommandBind(@NotNull CommandSender sender, @Nullable String[] args) {
        super("getqrcode", sender, args, 1, 2, "/dglab bind [player] -- 获取二维码地图绑定app", "dglab.bind");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (!(sender instanceof Player)) throw new CommandException("服务器后台请查看插件配置文件夹");
        this.player = (Player) sender;
        if (length == 1) {
            this.bindPlayer = player;
        }
        if (length == 2) {
            this.bindPlayer = getPlayer(args[1]);
            if (bindPlayer == null) throw new CommandException("玩家不存在");
        }

        if (player.getInventory().getItemInMainHand().getType() != Material.AIR)
            throw new CommandException("请空手执行该指令");
        if (!sender.hasPermission("dglab.bind.others") && !Objects.equals(bindPlayer, sender))
            throw new CommandException("你没有权限控制其他玩家");
    }

    @Override
    protected void run() {
        try {
            String url = getPlayerUrl(bindPlayer);
            BufferedImage img = generateQRCode(url, 128);
            giveMap(player, img, "§r二维码地图");
            sender.sendMessage("二维码生成成功 请使用app扫描");
        } catch (Exception e) {
            sender.sendMessage("二维码生成失败 未知错误");
            e.printStackTrace();
        }
    }

    @Override
    public List<String> tabComplete() {
        if (length == 2) return getPlayerList(sender);
        return null;
    }

}

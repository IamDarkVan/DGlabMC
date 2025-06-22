package shirohaNya.dglabmc.commands.impls;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.commands.CommandAbstract;
import shirohaNya.dglabmc.commands.CommandException;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

import static shirohaNya.dglabmc.DGlabMC.plugin;
import static shirohaNya.dglabmc.utils.DGlabUtils.generateQRCode;

public class CommandGetQRCode extends CommandAbstract {
    Player player;

    public CommandGetQRCode(@NotNull CommandSender sender, @Nullable String[] args) {
        super("getQRCode", sender, args, null, null, "/dglab getqrcode -- 获取二维码地图", "dglab.getQRcode");
    }

    @Override
    protected void errorHandle() throws CommandException {
        if (!(sender instanceof Player)) throw new CommandException("服务器后台请查看插件配置文件夹");
        this.player = (Player) sender;
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {    // 主手非空
            player.sendMessage("请空手执行该指令");
        }

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

    private void giveMap(Player player, BufferedImage image) {
        World world = player.getWorld();
        MapView view = Bukkit.createMap(world);
        view.getRenderers().forEach(view::removeRenderer);         // 清掉默认渲染器
        view.addRenderer(new MapRenderer() {
            private final BufferedImage img = image;
            private boolean done = false;
            @Override
            public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
                if (done) return;
                mapCanvas.drawImage(0, 0, img);
                done = true;
            }
        });

        ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
        MapMeta meta = (MapMeta) Objects.requireNonNull(mapItem.getItemMeta());
        meta.setMapView(view);
        meta.setDisplayName("§r二维码地图: ");
        mapItem.setItemMeta(meta);
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), mapItem);
        player.updateInventory();
    }
}

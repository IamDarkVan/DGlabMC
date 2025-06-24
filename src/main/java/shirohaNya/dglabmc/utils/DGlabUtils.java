package shirohaNya.dglabmc.utils;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import shirohaNya.dglabmc.MCWebSocketServer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;
import static shirohaNya.dglabmc.DGlabMC.plugin;

public class DGlabUtils {
    private DGlabUtils() throws Exception {
        throw new Exception("工具类不允许实例化");
    }

    public static void runWebSocketServer(int port) {
        plugin.mcWebSocketServer = new MCWebSocketServer(port);
        try {
            plugin.mcWebSocketServer.start();
        } catch (Exception e) {
            plugin.mcWebSocketServer.stop();
            plugin.mcWebSocketServer = null;
            getLogger().info("开启WebSocketServer失败" + e);
        }
    }

    public static void reloadConfigFile() {
        getLogger().info("正在重载");
        plugin.reloadConfig();
        plugin.config = plugin.getConfig();
    }

    @Deprecated
    public static <K, V> K getKeyByValue(@NotNull Map<K, V> map, @NotNull V value) {
        return map.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), value)).findFirst().map(Map.Entry::getKey).orElse(null);
    }

    public static String toDGJson(String type, String clientId, String targetId, String message) {
        return "{\"type\":\"" + type + "\",\"clientId\":\"" + clientId + "\",\"targetId\":\"" + targetId + "\",\"message\":\"" + message + "\"}";
    }

    public static String toDGJson(String[] str) {
        return "{\"type\":\"" + str[0] + "\",\"clientId\":\"" + str[1] + "\",\"targetId\":\"" + str[2] + "\",\"message\":\"" + str[3] + "\"}";
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, String> toHashMap(String json) {
        return new Gson().fromJson(json, HashMap.class);
    }

    public static void generateQRCodeFile(String text, String filePath) throws WriterException, IOException {
        String FILE_FORMAT = "PNG";
        int WIDTH = 128;
        int HEIGHT = 128;
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        File outputFile = new File(filePath);
        ImageIO.write(image, FILE_FORMAT, outputFile);
    }

    public static BufferedImage generateQRCode(String text, int size) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    public static void giveMap(Player player, BufferedImage image) {
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

        Material mapMaterial;
        try {
            //1.13+
            mapMaterial = Material.valueOf("FILLED_MAP");
        } catch (IllegalArgumentException e) {
            //1.9-1.12
            mapMaterial = Material.valueOf("MAP");
        }
        ItemStack mapItem = new ItemStack(mapMaterial);
        MapMeta mapMeta = (MapMeta) Objects.requireNonNull(mapItem.getItemMeta());
        try {
            Method mapViewMethod = mapMeta.getClass().getMethod("setMapView", MapView.class);
            mapViewMethod.invoke(mapMeta, view);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            try {
                Method getIdMethod = view.getClass().getMethod("getId");
                Object viewId = getIdMethod.invoke(view);
                short mapId;
                if (viewId instanceof Integer) {
                    mapId = (short) (int) viewId;
                } else if (viewId instanceof Short) {
                    mapId = (short) viewId;
                } else {
                    mapId = -1;
                }
                mapItem.setDurability(mapId);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {

            }
        }
        mapMeta.setDisplayName("§r二维码地图: ");
        mapItem.setItemMeta(mapMeta);
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), mapItem);
        player.updateInventory();
    }
}

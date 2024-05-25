package darkvan.dglabmc.utils;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import darkvan.dglabmc.Client;
import darkvan.dglabmc.MCWebSocketServer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static darkvan.dglabmc.DGlabMC.clients;
import static darkvan.dglabmc.DGlabMC.plugin;
import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getOnlinePlayers;

public class DGlabUtils {
    private DGlabUtils() throws Exception {
        throw new Exception("工具类不允许实例化");
    }
    public static void runWebSocketServer(int port) {
        plugin.mcWebSocketServer = new MCWebSocketServer(port);
        try {
            plugin.mcWebSocketServer.start();
        } catch (Exception e){
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
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null; // 如果没有找到对应的键，返回null
    }

    public static String toDGJson(String type, String clientId, String targetId, String message){
        return "{\"type\":\"" + type + "\",\"clientId\":\"" + clientId + "\",\"targetId\":\"" + targetId + "\",\"message\":\"" + message +"\"}";
    }

    public static String toDGJson(String[] str){
        return "{\"type\":\"" + str[0] + "\",\"clientId\":\"" + str[1] + "\",\"targetId\":\"" + str[2] + "\",\"message\":\"" + str[3] +"\"}";
    }

    public static List<String> playerAndClients(){
        return Stream.concat(getOnlinePlayers().stream().map(Player::getName), clients.stream().map(Client::getClientId)).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String,String> toHashMap(String json){
        return new Gson().fromJson(json,HashMap.class);
    }

    public static void generateQRCode(String text, String filePath) throws WriterException, IOException {
        String FILE_FORMAT = "PNG";
        int WIDTH = 200;
        int HEIGHT = 200;
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
}

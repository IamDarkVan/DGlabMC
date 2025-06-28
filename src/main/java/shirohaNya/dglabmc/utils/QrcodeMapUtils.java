package shirohaNya.dglabmc.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Objects;

import static shirohaNya.dglabmc.ConfigManager.getIp;
import static shirohaNya.dglabmc.ConfigManager.getPort;

public class QrcodeMapUtils {
    private QrcodeMapUtils() throws Exception {
        throw new Exception("工具类不允许实例化");
    }

    public static String getPlayerUrl(Player player) {
        return "https://www.dungeon-lab.com/app-download.php#DGLAB-SOCKET#" + "ws://" + getIp() + ":" + getPort() + "/" + player.getUniqueId();
    }

    public static BufferedImage generateQRCode(String text, int size) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    @SuppressWarnings("deprecation")
    public static void giveMap(Player player, BufferedImage image, String title) {
        World world = player.getWorld();
        MapView view = Bukkit.createMap(world);
        view.getRenderers().forEach(view::removeRenderer);         // 清掉默认渲染器
        view.addRenderer(new MapRenderer() {
            private boolean done = false;

            @Override
            public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
                if (done) return;
                mapCanvas.drawImage(0, 0, image);
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
            mapViewMethod.setAccessible(true);
            mapViewMethod.invoke(mapMeta, view);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            try {
                Method getIdMethod = view.getClass().getMethod("getId");
                getIdMethod.setAccessible(true);
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
        mapMeta.setDisplayName(title);
        mapMeta.setLore(Collections.singletonList("§7DGLAB二维码地图"));
        mapItem.setItemMeta(mapMeta);
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), mapItem);
        player.updateInventory();
    }

    public static boolean isQrcodeMap(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.hasLore() && Objects.requireNonNull(meta.getLore()).contains("§7DGLAB二维码地图");
    }

    public static void removeMap(ItemStack map, @Nullable Inventory inv) {
        assert inv != null;
        inv.remove(map);
        removeMapView(map);
    }

    public static void removeMapView(ItemStack map) {
        // 还可同时删除 MapView 上的渲染器以避免内存泄漏
        MapView view = getMapView(map);
        if (view != null) view.getRenderers().clear();
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    private static MapView getMapView(ItemStack map) {
        try {
            MapMeta meta = (MapMeta) map.getItemMeta();
            assert meta != null;
            Method m = meta.getClass().getMethod("getMapView");
            m.setAccessible(true);
            return (MapView) m.invoke(meta);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            try {
                Method getDurability = ItemStack.class.getMethod("getDurability");
                getDurability.setAccessible(true);
                Method getMapMethod = Bukkit.class.getMethod("getMap", short.class); // 注意是 short
                getMapMethod.setAccessible(true);
                short durability = (short) getDurability.invoke(map);
                return (MapView) getMapMethod.invoke(null, durability);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

}

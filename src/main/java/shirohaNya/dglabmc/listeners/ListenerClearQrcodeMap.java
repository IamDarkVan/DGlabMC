package shirohaNya.dglabmc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import shirohaNya.dglabmc.utils.QrcodeMapUtils;

import static shirohaNya.dglabmc.utils.QrcodeMapUtils.*;

public class ListenerClearQrcodeMap implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        if (isQrcodeMap(e.getCurrentItem())) removeMap(e.getCurrentItem(), e.getClickedInventory());
        if (isQrcodeMap(e.getCursor())) removeMap(e.getCursor(), e.getClickedInventory());
        int hotbar = e.getHotbarButton();
        if (hotbar >= 0 && hotbar <= 8 && isQrcodeMap(p.getInventory().getItem(hotbar))) removeMap(p.getInventory().getItem(e.getHotbarButton()), p.getInventory());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        for (ItemStack item : e.getNewItems().values()) {
            if (isQrcodeMap(item)) {
                removeMap(item, e.getInventory());
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (isQrcodeMap(e.getItemDrop().getItemStack())) {
            e.getItemDrop().remove();
            removeMapView(e.getItemDrop().getItemStack());
        }
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent e) {
        if (isQrcodeMap(e.getOffHandItem())) {
            removeMap(e.getOffHandItem(), e.getPlayer().getInventory());
        }
        if (isQrcodeMap(e.getMainHandItem())){
            removeMap(e.getMainHandItem(), e.getPlayer().getInventory());
        }
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack previous = p.getInventory().getItem(e.getPreviousSlot());
        if (isQrcodeMap(previous)) removeMap(previous, p.getInventory());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Inventory inv = e.getPlayer().getInventory();
        for (ItemStack slot : inv) if (isQrcodeMap(slot)) removeMap(slot, inv);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Inventory inv = e.getPlayer().getInventory();
        for (ItemStack slot : inv) if (isQrcodeMap(slot)) removeMap(slot, inv);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(QrcodeMapUtils::isQrcodeMap);
        for (ItemStack i : event.getDrops()) if (isQrcodeMap(i)) removeMapView(i);
    }
}

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
import shirohaNya.dglabmc.utils.DGlabUtils;

import static shirohaNya.dglabmc.utils.DGlabUtils.*;

public class ListenerClearMap implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (isQrcodeMap(e.getCurrentItem())) removeMap(e.getCurrentItem(), e.getClickedInventory());
        if (isQrcodeMap(e.getCursor())) removeMap(e.getCursor(), e.getClickedInventory());
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
            e.setCancelled(true);
            e.getItemDrop().remove();
            removeMapView(e.getItemDrop().getItemStack());
        }
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent e) {
        if (isQrcodeMap(e.getOffHandItem())) {
            e.setOffHandItem(null);
            removeMapView(e.getOffHandItem());
        }
        if (isQrcodeMap(e.getMainHandItem())){
            e.setMainHandItem(null);
            removeMapView(e.getMainHandItem());
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
        event.getDrops().removeIf(DGlabUtils::isQrcodeMap);
        for (ItemStack i : event.getDrops()) if (isQrcodeMap(i)) removeMapView(i);
    }
}

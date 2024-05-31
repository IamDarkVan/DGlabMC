package darkvan.dglabmc.listeners;

import darkvan.dglabmc.Client;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static darkvan.dglabmc.utils.ClientUtils.getClient;
import static darkvan.dglabmc.utils.ClientUtils.isClientExist;

public class ListenerUnbindOfflinePlayer implements Listener {
    @EventHandler
    public void onPlayerOffline(PlayerQuitEvent e) {
        if (!isClientExist(e.getPlayer())) return;
        Client client = getClient(e.getPlayer());
        client.bind(null);
    }
}

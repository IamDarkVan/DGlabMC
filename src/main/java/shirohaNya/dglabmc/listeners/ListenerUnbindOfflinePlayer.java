package shirohaNya.dglabmc.listeners;

import shirohaNya.dglabmc.api.Client;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static shirohaNya.dglabmc.client.ClientManager.getClient;
import static shirohaNya.dglabmc.client.ClientManager.isClientExist;

public class ListenerUnbindOfflinePlayer implements Listener {
    @EventHandler
    public void onPlayerOffline(PlayerQuitEvent e) {
        if (!isClientExist(e.getPlayer())) return;
        Client client = getClient(e.getPlayer());
        client.unbind();
    }
}

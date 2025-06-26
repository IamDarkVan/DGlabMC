package shirohaNya.dglabmc.listeners;

import shirohaNya.dglabmc.Client;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static shirohaNya.dglabmc.utils.ClientUtils.getClient;
import static shirohaNya.dglabmc.utils.ClientUtils.isClientExist;

public class ListenerUnbindOfflinePlayer implements Listener {
    @EventHandler
    public void onPlayerOffline(PlayerQuitEvent e) {
        if (!isClientExist(e.getPlayer())) return;
        Client client = getClient(e.getPlayer());
        client.unbind();
    }
}

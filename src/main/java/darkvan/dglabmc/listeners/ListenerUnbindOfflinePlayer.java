package darkvan.dglabmc.listeners;

import darkvan.dglabmc.Client;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static darkvan.dglabmc.utils.ClientUtils.getClientByPlayer;
import static darkvan.dglabmc.utils.ClientUtils.isClientPlayerExist;

public class ListenerUnbindOfflinePlayer implements Listener {
    @EventHandler
    public void onPlayerOffline(PlayerQuitEvent e){
        if (!isClientPlayerExist(e.getPlayer())) return;
        Client client = getClientByPlayer(e.getPlayer());
        client.bind(null);
    }
}

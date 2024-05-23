package darkvan.dglabmc.listeners;

import darkvan.dglabmc.Client;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import static darkvan.dglabmc.DGlabMC.mcUUID;
import static darkvan.dglabmc.utils.ClientUtils.getClientByPlayer;
import static darkvan.dglabmc.utils.ClientUtils.isClientPlayerExist;
import static darkvan.dglabmc.utils.DGlabUtils.toDGJson;

public class ListenerGame1 implements Listener {


    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e){
        if (!(e.getEntity() instanceof Player player) || !isClientPlayerExist(player)) return;
        playerDamageHandler(e.getDamage(), player);
    }
    @EventHandler
    public void onPlayerHealthRegain(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player player) || !isClientPlayerExist(player)) return;
        playerHealthRegainHandler(e.getAmount(), player);
    }

    public static void playerDamageHandler(double damage, Player player) {
        Client client = getClientByPlayer(player);
        int valueA = (int) ((1 - (player.getHealth() - damage) / player.getMaxHealth()) * client.getAMaxStrength());
        int valueB = (int) ((1 - (player.getHealth() - damage) / player.getMaxHealth()) * client.getBMaxStrength());
        client.output(toDGJson("msg", mcUUID, client.getClientId(), "strength-1+2+" + valueA));
        client.output(toDGJson("msg", mcUUID, client.getClientId(), "strength-2+2+" + valueB));
        client.giveShock(5, true);
    }
    public static void playerHealthRegainHandler(double amount, Player player) {
        Client client = getClientByPlayer(player);
        int valueA = (int) ((1 - (player.getHealth() + amount) / player.getMaxHealth()) * client.getAMaxStrength());
        int valueB = (int) ((1 - (player.getHealth() + amount) / player.getMaxHealth()) * client.getBMaxStrength());
        client.output(toDGJson("msg", mcUUID, client.getClientId(), "strength-1+2+" + valueA));
        client.output(toDGJson("msg", mcUUID, client.getClientId(), "strength-2+2+" + valueB));
        if (valueA == 0 && valueB == 0) client.giveShock(0);
    }
}

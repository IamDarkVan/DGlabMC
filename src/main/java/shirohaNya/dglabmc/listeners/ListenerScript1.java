package shirohaNya.dglabmc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import shirohaNya.dglabmc.api.Client;
import shirohaNya.dglabmc.enums.AdjustMode;
import shirohaNya.dglabmc.enums.Channel;
import shirohaNya.dglabmc.api.Script;

import static shirohaNya.dglabmc.scripts.ScriptManager.getScript;
import static shirohaNya.dglabmc.client.ClientManager.getClient;
import static shirohaNya.dglabmc.client.ClientManager.isClientExist;

public class ListenerScript1 implements Listener {
    private Player player;
    private final Script script = getScript("script1");

    @SuppressWarnings("deprecation")
    public static void playerDamageHandler(double damage, Player player) {
        Client client = getClient(player);
        int valueA = (int) ((1 - (player.getHealth() - damage) / player.getMaxHealth()) * client.getAMaxStrength());
        int valueB = (int) ((1 - (player.getHealth() - damage) / player.getMaxHealth()) * client.getBMaxStrength());
        client.adjustStrength(Channel.A, AdjustMode.SET, valueA);
        client.adjustStrength(Channel.B, AdjustMode.SET, valueB);
        client.giveShock(Channel.BOTH, 5, true);
    }

    @SuppressWarnings("deprecation")
    public static void playerHealthRegainHandler(double amount, Player player) {
        Client client = getClient(player);
        int valueA = (int) ((1 - (player.getHealth() + amount) / player.getMaxHealth()) * client.getAMaxStrength());
        int valueB = (int) ((1 - (player.getHealth() + amount) / player.getMaxHealth()) * client.getBMaxStrength());
        client.adjustStrength(Channel.A, AdjustMode.SET, valueA);
        client.adjustStrength(Channel.B, AdjustMode.SET, valueB);
        if (valueA == 0 && valueB == 0) client.giveShock(Channel.BOTH, 0, true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) player = (Player) e.getEntity();
        else return;
        if (!isClientExist(player) || !script.isClientEnabled(getClient(player))) return;
        playerDamageHandler(e.getDamage(), player);
    }

    @EventHandler
    public void onPlayerHealthRegain(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) player = (Player) e.getEntity();
        else return;
        if (!isClientExist(player) || !script.isClientEnabled(getClient(player))) return;
        playerHealthRegainHandler(e.getAmount(), player);
    }
}

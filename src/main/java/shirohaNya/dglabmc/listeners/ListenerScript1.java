package shirohaNya.dglabmc.listeners;

import shirohaNya.dglabmc.Client;
import shirohaNya.dglabmc.scripts.Script;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import static shirohaNya.dglabmc.DGlabMC.mcUUID;
import static shirohaNya.dglabmc.scripts.ScriptManager.getScript;
import static shirohaNya.dglabmc.utils.ClientUtils.getClient;
import static shirohaNya.dglabmc.utils.ClientUtils.isClientExist;
import static shirohaNya.dglabmc.utils.DGlabUtils.toDGJson;

public class ListenerScript1 implements Listener {
    private Player player;
    private final Script script = getScript("script1");
    @SuppressWarnings("deprecation")
    public static void playerDamageHandler(double damage, Player player) {
        Client client = getClient(player);
        int valueA = (int) ((1 - (player.getHealth() - damage) / player.getMaxHealth()) * client.getAMaxStrength());
        int valueB = (int) ((1 - (player.getHealth() - damage) / player.getMaxHealth()) * client.getBMaxStrength());
        client.output(toDGJson("msg", mcUUID, client.getClientId(), "strength-1+2+" + valueA));
        client.output(toDGJson("msg", mcUUID, client.getClientId(), "strength-2+2+" + valueB));
        client.giveShock(5, true);
    }

    @SuppressWarnings("deprecation")
    public static void playerHealthRegainHandler(double amount, Player player) {
        Client client = getClient(player);
        int valueA = (int) ((1 - (player.getHealth() + amount) / player.getMaxHealth()) * client.getAMaxStrength());
        int valueB = (int) ((1 - (player.getHealth() + amount) / player.getMaxHealth()) * client.getBMaxStrength());
        client.output(toDGJson("msg", mcUUID, client.getClientId(), "strength-1+2+" + valueA));
        client.output(toDGJson("msg", mcUUID, client.getClientId(), "strength-2+2+" + valueB));
        if (valueA == 0 && valueB == 0) client.giveShock(0);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) player = (Player) e.getEntity(); else return;
        if (!isClientExist(player) || !script.isClientEnabled(getClient(player))) return;
        playerDamageHandler(e.getDamage(), player);
    }

    @EventHandler
    public void onPlayerHealthRegain(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) player = (Player) e.getEntity(); else return;
        if (!isClientExist(player) || !script.isClientEnabled(getClient(player))) return;
        playerHealthRegainHandler(e.getAmount(), player);
    }
}

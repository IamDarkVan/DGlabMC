package darkvan.dglabmc.listeners;

import darkvan.dglabmc.Client;
import darkvan.dglabmc.scripts.ScriptAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import static darkvan.dglabmc.DGlabMC.mcUUID;
import static darkvan.dglabmc.scripts.ScriptManager.getScriptManager;
import static darkvan.dglabmc.utils.ClientUtils.getClient;
import static darkvan.dglabmc.utils.ClientUtils.isClientExist;
import static darkvan.dglabmc.utils.DGlabUtils.toDGJson;

public class ListenerScript1 implements Listener {
    private Player player;
    private final ScriptAbstract script = getScriptManager().getScript("script1");
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

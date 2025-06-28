package shirohaNya.dglabmc.scripts.impls;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.api.Client;
import shirohaNya.dglabmc.enums.BossbarType;
import shirohaNya.dglabmc.enums.Channel;
import shirohaNya.dglabmc.scripts.ScriptAbstract;
import shirohaNya.dglabmc.scripts.impls.script2Tools.SharedClient;

import static shirohaNya.dglabmc.client.ClientManager.clients;
import static shirohaNya.dglabmc.utils.QrcodeMapUtils.isQrcodeMap;
import static shirohaNya.dglabmc.utils.QrcodeMapUtils.removeMap;

public class Script2 extends ScriptAbstract{
    public Script2() {
        super("script2", "把B通道共享给另一个玩家", null);
    }

    @Override
    public boolean onEnable(Client client) {
        if (client instanceof SharedClient) return true;
        if (client.getPlayer() == null) return false;
        Player player = getSharePlayer(client.getPlayer(), getSettings().getInt("range"));
        if (player == null) {
            client.sendMessage("未找到目标, 请让共享目标使用 /dglab bind 手持二维码地图并紧贴目标");
            return false;
        }
        SharedClient sharedClient = new SharedClient(player, client);

        sharedClient.setPulse(Channel.A, null);
        sharedClient.setPulse(Channel.B, client.getBPulse());
        client.setPulse(Channel.B, null);

        client.getBossbar().setBossbarType(BossbarType.A);
        sharedClient.getBossbar().setBossbarType(BossbarType.B);

        super.enableClient(sharedClient);

        ItemStack map = player.getInventory().getItemInMainHand();
        if (isQrcodeMap(map)) removeMap(map, player.getInventory());
        return true;
    }

    @Override
    public boolean onDisable(Client client) {
        Client c1;
        SharedClient c2;
        if (client instanceof SharedClient) {
            c2 = (SharedClient) client;
            c1 = c2.getSourceClient();
        } else {
            c1 = client;
            c2 = (SharedClient) clients.stream()
                    .filter(cli -> cli instanceof SharedClient && ((SharedClient) cli).getSourceClient().equals(c1))
                    .findFirst().orElse(null);
        }
        c1.setPulse(Channel.B, c1.getAPulse());
        super.disableClient(c1);
        if (c2 != null) c2.unbind();
        return true;
    }

    private @Nullable Player getSharePlayer(Player source, double range) {
        for (Entity e : source.getNearbyEntities(range, range, range)){
            if (!(e instanceof Player)) continue;
            Player p = (Player) e;
            if (p == source || !p.isOnline() || !isQrcodeMap(p.getInventory().getItemInMainHand())) continue;
            return p;
        }
        return null;
    }
}

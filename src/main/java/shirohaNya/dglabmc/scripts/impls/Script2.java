package shirohaNya.dglabmc.scripts.impls;

import org.bukkit.entity.Player;
import shirohaNya.dglabmc.Client;
import shirohaNya.dglabmc.scripts.ScriptAbstract;

import java.util.HashMap;

public class Script2 extends ScriptAbstract {
    public Script2() {
        super("script2", "把郊狼B通道分配给另一个玩家", null);
    }
    private final HashMap<Client, Player> sharedPlayerMap = new HashMap<>();

    @Override
    public void onEnable(Client client) {
        //TODO 如何绑定共享玩家


    }

    @Override
    public void onDisable(Client client) {

    }
}

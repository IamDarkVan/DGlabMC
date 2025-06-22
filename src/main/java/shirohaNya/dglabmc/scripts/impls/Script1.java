package shirohaNya.dglabmc.scripts.impls;

import shirohaNya.dglabmc.Client;
import shirohaNya.dglabmc.scripts.Script;
import shirohaNya.dglabmc.scripts.ScriptAbstract;
import lombok.Getter;

@Getter
public class Script1 extends ScriptAbstract {
    private final Script script1 = new Script1();
    public Script1() {
        // 电击时间: 5秒, 受伤后重置电击时间(否则累加): true
        super("script1", "受伤电人", null);
    }

    @Override
    public void onEnable(Client client) {

    }

    @Override
    public void onDisable(Client client) {

    }
}

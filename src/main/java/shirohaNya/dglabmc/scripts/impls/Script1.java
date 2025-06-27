package shirohaNya.dglabmc.scripts.impls;

import lombok.Getter;
import shirohaNya.dglabmc.api.Client;
import shirohaNya.dglabmc.scripts.ScriptAbstract;

@Getter
public class Script1 extends ScriptAbstract {
    public Script1(){
        // 电击时间: 5秒, 受伤后重置电击时间(否则累加): true
        super("script1", "受伤电人", null);
    }

    @Override
    public boolean onEnable(Client client) {
        client.sendMessage("你已启用脚本script1");
        return true;
    }

    @Override
    public boolean onDisable(Client client) {
        client.sendMessage("你已禁用脚本script1");
        return true;
    }
}

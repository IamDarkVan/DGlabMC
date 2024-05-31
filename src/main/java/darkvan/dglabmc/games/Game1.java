package darkvan.dglabmc.games;

import darkvan.dglabmc.Client;

public class Game1 extends Game{
    public static final Game game1 = new Game1();
    private Game1() {
        // 电击时间: 5秒, 受伤后重置电击时间(否则累加): true
        super("game1","受伤电人", null);
    }

    @Override
    public void onEnable(Client client) {

    }

    @Override
    public void onDisable(Client client) {

    }
}

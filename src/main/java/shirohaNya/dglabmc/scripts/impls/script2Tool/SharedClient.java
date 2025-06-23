package shirohaNya.dglabmc.scripts.impls.script2Tool;

import org.bukkit.entity.Player;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.Client;

public class SharedClient extends Client {
    public SharedClient(String clientId, WebSocket webSocket, @Nullable Player player) {
        super(clientId, webSocket, player);
    }

}

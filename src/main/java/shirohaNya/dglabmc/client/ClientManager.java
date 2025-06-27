package shirohaNya.dglabmc.client;

import org.bukkit.entity.Player;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.api.Client;

import java.util.HashSet;
import java.util.Objects;

public class ClientManager {
    public static final HashSet<Client> clients = new HashSet<>();

    private ClientManager() throws Exception {
        throw new Exception("管理类不允许实例化");
    }

    public static Client createClient(String targetId, WebSocket webSocket) {
        return createClient(targetId, webSocket, null);
    }

    public static Client createClient(String targetId, WebSocket webSocket, Player player) {
        return new ClientImpl(targetId, webSocket, player);
    }

    public static @NotNull Client getClient(@Nullable String id) throws RuntimeException {
        for (Client cli : clients) if (Objects.equals(cli.getTargetId(), id)) return cli;
        throw new RuntimeException("未找到客户端");
    }

    public static @NotNull Client getClient(@Nullable WebSocket ws) throws RuntimeException {
        for (Client cli : clients) if (cli.getWebSocket() == ws) return cli;
        throw new RuntimeException("未找到客户端");
    }

    public static @NotNull Client getClient(@Nullable Player p) throws RuntimeException {
        for (Client cli : clients) if (cli.getPlayer() == p) return cli;
        throw new RuntimeException("未找到客户端");
    }

    public static boolean isClientExist(@Nullable String id) {
        return clients.stream().anyMatch(cli -> Objects.equals(cli.getTargetId(), id));
    }

    public static boolean isClientExist(@Nullable WebSocket ws) {
        return clients.stream().anyMatch(cli -> cli.getWebSocket() == ws);
    }

    public static boolean isClientExist(@Nullable Player p) {
        return clients.stream().anyMatch(cli -> cli.getPlayer() == p);
    }
}

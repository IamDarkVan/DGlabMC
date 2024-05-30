package darkvan.dglabmc.utils;

import darkvan.dglabmc.Client;
import org.bukkit.entity.Player;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static darkvan.dglabmc.DGlabMC.clients;

public class ClientUtils {
    private ClientUtils() throws Exception {
        throw new Exception("工具类不允许实例化");
    }
    public static Client createClient(String clientId, WebSocket webSocket, @Nullable Player player){
        return new Client(clientId,webSocket,player);
    }
    public static @NotNull Client getClient(@Nullable String id) throws RuntimeException {
        for (Client cli : clients) if (Objects.equals(cli.getClientId(), id)) return cli;
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
    public static boolean isClientExist(@Nullable String id){
        return clients.stream().anyMatch(cli -> Objects.equals(cli.getClientId(), id));
    }
    public static boolean isClientExist(@Nullable WebSocket ws){
        return clients.stream().anyMatch(cli -> cli.getWebSocket() == ws);
    }
    public static boolean isClientExist(@Nullable Player p){
        return clients.stream().anyMatch(cli -> cli.getPlayer() == p);
    }
}

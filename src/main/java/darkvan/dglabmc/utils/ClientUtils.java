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
    public static @NotNull Client getClientById(@Nullable String id) throws RuntimeException {
        for (Client cli : clients) if (Objects.equals(cli.getClientId(), id)) return cli;
        throw new RuntimeException("未找到客户端");
    }
    public static @NotNull Client getClientByWebSocket(@Nullable WebSocket ws) throws RuntimeException {
        for (Client cli : clients) if (cli.getWebSocket() == ws) return cli;
        throw new RuntimeException("未找到客户端");
    }
    public static @NotNull Client getClientByPlayer(@Nullable Player p) throws RuntimeException {
        for (Client cli : clients) if (cli.getPlayer() == p) return cli;
        throw new RuntimeException("未找到客户端");
    }
    public static boolean isClientIdExist(@Nullable String id){
        for (Client cli : clients) if (Objects.equals(cli.getClientId(), id)) return true;
        return false;
    }
    @Deprecated
    public static boolean isClientWebsocketExist(@Nullable WebSocket ws){
        for (Client cli : clients) if (cli.getWebSocket() == ws) return true;
        return false;
    }
    public static boolean isClientPlayerExist(@Nullable Player p){
        for (Client cli : clients) if (cli.getPlayer() == p) return true;
        return false;
    }
}

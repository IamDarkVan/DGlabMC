package shirohaNya.dglabmc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import shirohaNya.dglabmc.utils.ClientUtils;
import lombok.SneakyThrows;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.*;

import static shirohaNya.dglabmc.DGlabMC.plugin;
import static shirohaNya.dglabmc.utils.ClientUtils.*;
import static shirohaNya.dglabmc.utils.DGlabUtils.*;
import static org.bukkit.Bukkit.getLogger;

public class MCWebSocketServer extends WebSocketServer {

    public MCWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    @SneakyThrows
    public void stop() {
        plugin.mcWebSocketServer = null;
        super.stop(0);
        getLogger().info("服务器停止运行");
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake handshake) {
        String clientID = UUID.randomUUID().toString();
        getLogger().info("New WebSocket connection has been created, UUID:" + clientID);
        Client client = createClient(clientID, ws);
        client.output(toDGJson("bind", clientID, "", "targetId"));
    }

    @Override
    public void onClose(@NotNull WebSocket ws, int code, String reason, boolean remote) {
        ClientUtils.getClient(ws).removeClient();
    }

    @Override
    public void onMessage(WebSocket ws, String text) {
        Client client = ClientUtils.getClient(ws);
        if (plugin.logInputMessage) getLogger().info("服务器收到: " + client.getTargetId() + ": " + text);
        HashMap<String, String> data;
        //输入消息异常处理
        try {
            data = toHashMap(text);
        } catch (Exception e) {
            client.output(toDGJson("msg", "", "", "403"));
            if (plugin.logInputMessage) getLogger().info("该消息非JSON,已作废 403");
            return;
        }
        if (!(data.keySet().containsAll(Arrays.asList("type", "clientId", "targetId", "message")))) {
            client.output(toDGJson("msg", "", "", "404"));
            if (plugin.logInputMessage) getLogger().info("该消息无必要键值,已作废 404(1)");
            return;
        }
        String type = data.get("type"), clientId = data.get("clientId"),
                targetId = data.get("targetId"), message = data.get("message");
        if (!isValidUUID(clientId) || !isValidUUID(targetId)) {
            client.output(toDGJson("msg", "", "", "404"));
            if (plugin.logInputMessage) getLogger().info("该消息来源非UUID,已作废 404(2)");
        }
        if (!isClientExist(targetId) || getClient(targetId).getWebSocket() != ws) {
            client.output(toDGJson("msg", "", "", "404"));
            if (plugin.logInputMessage) getLogger().info("该消息来源未知,已作废 404(3)");
            return;
        }
        // bind绑定玩家
        if (Objects.equals(type, "bind")) {
            Player player = Bukkit.getPlayer(clientId);
            if (player == null) {
                client.output(toDGJson("bind", clientId, targetId, "400"));
                if (plugin.logInputMessage) getLogger().info("该消息未知或玩家不在线,已作废 400");
                return;
            }
            client.output(toDGJson("bind", clientId, targetId, "200"));
            if (plugin.logInputMessage) getLogger().info("成功连接 200");
            client.bind(player);
        }
        // msg消息
        if (Objects.equals(type, "msg")) {
            if (message.contains("strength")) {
                // strength-0+1+2+3, [0, 1, 2, 3]
                Integer[] strength = Arrays.stream(message.split("-")[1].split("\\+")).map(Integer::parseInt).toArray(Integer[]::new);
                client.setAStrength(strength[0]);
                client.setBStrength(strength[1]);
                client.setAMaxStrength(strength[2]);
                client.setBMaxStrength(strength[3]);
                if (client.getPlayer() == null) return;
                client.resetBossbarTitle();
                return;
            }
            if (message.contains("feedback")) {
                // 0 ~ 4 通道A, 5 ~ 9 通道B
                String fb = message.split("-")[1];
                getLogger().info(fb);
                //待实现
                client.sendMessage("你按下了 " + fb);
            }
        }
    }


    @Override
    public void onError(WebSocket ws, Exception ex) {
        getLogger().info("操你妈报错了: " + ex);
        ex.printStackTrace();
        stop();
    }

    @SneakyThrows
    @Override
    public void onStart() {
        getLogger().info("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
        getLogger().info("WebSocket Server started on port: " + plugin.port);
    }
}



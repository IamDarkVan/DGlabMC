package shirohaNya.dglabmc.client;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.api.Client;
import shirohaNya.dglabmc.enums.AdjustMode;
import shirohaNya.dglabmc.enums.Channel;
import shirohaNya.dglabmc.api.Script;
import shirohaNya.dglabmc.scripts.ScriptManager;

import java.util.HashSet;
import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;
import static shirohaNya.dglabmc.ConfigManager.isLogOutputMessage;
import static shirohaNya.dglabmc.client.ClientManager.clients;
import static shirohaNya.dglabmc.DGlabMC.plugin;
import static shirohaNya.dglabmc.utils.DGlabUtils.toDGJson;

@Getter
public class ClientImpl implements Client {
    @Nullable
    private Player player;
    private String playerId;
    private final String targetId;
    private final WebSocket webSocket;
    private final BossbarManager bossbar = new BossbarManager();
    private final HashSet<Script> enabledScripts = new HashSet<>();
    private Integer aStrength = 0;
    private Integer bStrength = 0;
    private Integer aMaxStrength = 0;
    private Integer bMaxStrength = 0;
    private String aPulse = "[0A0A0A0A00000000,0A0A0A0A0A0A0A0A,0A0A0A0A14141414,0A0A0A0A1E1E1E1E,0A0A0A0A28282828,0A0A0A0A32323232,0A0A0A0A3C3C3C3C,0A0A0A0A46464646,0A0A0A0A50505050,0A0A0A0A5A5A5A5A,0A0A0A0A64646464]";
    private String bPulse = "[0A0A0A0A00000000,0A0A0A0A0A0A0A0A,0A0A0A0A14141414,0A0A0A0A1E1E1E1E,0A0A0A0A28282828,0A0A0A0A32323232,0A0A0A0A3C3C3C3C,0A0A0A0A46464646,0A0A0A0A50505050,0A0A0A0A5A5A5A5A,0A0A0A0A64646464]";
    private double aTotalTime = 0, bTotalTime = 0;
    private int aTicks = 0, bTicks = 0;
    @Nullable
    private BukkitTask aShockTask, bShockTask;

    public ClientImpl(String targetId, WebSocket webSocket, @Nullable Player player) {
        this.targetId = targetId;
        this.webSocket = webSocket;
        this.player = player;
        clients.add(this);
        enabledScripts.addAll(ScriptManager.getDefaultScripts());
    }

    @Override
    public void output(String text) {
        if (isLogOutputMessage()) getLogger().info("服务器发出: " + text);
        webSocket.send(text);
    }

    @Override
    public void bind(@NotNull Player p) {
        this.player = p;
        this.playerId = p.getUniqueId().toString();
        updateBossbarTitle();
        bossbar.resetBossbar();
        bossbar.setPlayer(p);
        player.sendMessage("你已成功绑定app");
    }

    @Override
    public void unbind(){
        if (webSocket != null) webSocket.close();
        else removeClient();
    }

    @Override
    public void removeClient() {
        clients.remove(this);
        bossbar.resetBossbar();
        enabledScripts.clear();
        getLogger().info("已断开" + targetId + "的连接");
        if (player != null) player.sendMessage("你绑定的 " + targetId + " 已断开连接");
    }

    @Override
    public void sendMessage(String msg) {
        if (player == null) return;
        player.sendMessage(msg);
    }

    @Override
    public void giveShock(Channel channel, int sec, boolean replace) {
        if (sec == 0) {
            cancelShock(channel);
            return;
        }
        if (channel == Channel.A || channel == Channel.BOTH) {
            if (aPulse == null) return;
            if (replace) aTicks = 0;
            this.aTotalTime = replace ? sec : Math.max(aTotalTime + sec, 0);
            if (aShockTask == null) this.aShockTask = new BukkitRunnable() {
                @Override
                public void run() {
                    sendWave(Channel.A);
                    updateBossbarTitle();
                    if (aTicks >= aTotalTime * 20) {
                        cancelShock(Channel.A);
                        return;
                    }
                    bossbar.setAProgress(1.0 - (aTicks / (aTotalTime * 20)));
                    aTicks++;
                }
            }.runTaskTimerAsynchronously(plugin, 0, 1);
        }
        if (channel == Channel.B || channel == Channel.BOTH) {
            if (bPulse == null) return;
            if (replace) bTicks = 0;
            this.bTotalTime = replace ? sec : Math.max(bTotalTime + sec, 0);
            if (bShockTask == null) this.bShockTask = new BukkitRunnable() {
                @Override
                public void run() {
                    sendWave(Channel.B);
                    updateBossbarTitle();
                    if (bTicks >= bTotalTime * 20) {
                        cancelShock(Channel.B);
                        return;
                    }
                    bossbar.setBProgress(1.0 - (bTicks / (bTotalTime * 20)));
                    bTicks++;
                }
            }.runTaskTimerAsynchronously(plugin, 0, 1);
        }
    }

    @Override
    public void cancelShock(Channel channel) {
        clearWave(channel);
        if (channel == Channel.A || channel == Channel.BOTH) {
            bossbar.setAProgress(0);
            aTotalTime = 0;
            aTicks = 0;
            if (aShockTask != null) aShockTask.cancel();
            aShockTask = null;
        }
        if (channel == Channel.B || channel == Channel.BOTH) {
            bossbar.setBProgress(0);
            bTotalTime = 0;
            bTicks = 0;
            if (bShockTask != null) bShockTask.cancel();
            bShockTask = null;
        }
        updateBossbarTitle();
    }

    private void sendWave(Channel channel) {
        if ((channel == Channel.BOTH || channel == Channel.A) && aPulse != null)
            output(toDGJson("msg", playerId, targetId, "pulse-A:" + aPulse));
        if ((channel == Channel.BOTH || channel == Channel.B) && bPulse != null)
            output(toDGJson("msg", playerId, targetId, "pulse-B:" + bPulse));
    }

    private void clearWave(Channel channel) {
        if (channel == Channel.BOTH) {
            output(toDGJson("msg", playerId, targetId, "clear-1"));
            output(toDGJson("msg", playerId, targetId, "clear-2"));
            return;
        }
        output(toDGJson("msg", playerId, targetId, "clear-" + channel.getValue()));
    }

    @Override
    public void updateBossbarTitle() {
        double aTime = aTotalTime - aTicks / 20;
        double bTime = bTotalTime - bTicks / 20;
        bossbar.getABossbar().setTitle("A:" + aStrength + "/" + aMaxStrength +
                " 电击剩余时间:" + aTime + "秒");
        bossbar.getBBossbar().setTitle("B:" + bStrength + "/" + bMaxStrength +
                " 电击剩余时间:" + bTime + "秒");
    }

    //通道: 1 - A 通道；2 - B 通道
    //强度变化模式: 0 - 通道强度减少；1 - 通道强度增加；2 - 通道强度变化为指定数值
    //数值: 范围在(0 ~ 200)的整型
    @Override
    public void adjustStrength(Channel channel, AdjustMode type, int num) {
        //不会修改本地储存值 app会发送变更后强度信息
        if (channel == Channel.BOTH || channel == Channel.A)
            output(toDGJson("msg", playerId, targetId, "strength-1+" + type.getValue() + "+" + num));
        if (channel == Channel.BOTH || channel == Channel.B)
            output(toDGJson("msg", playerId, targetId, "strength-2+" + type.getValue() + "+" + num));
    }

    @Override
    public void setStrength(Channel channel, int num) {
        if (channel == Channel.BOTH || channel == Channel.A) this.aStrength = num;
        if (channel == Channel.BOTH || channel == Channel.B) this.bStrength = num;
    }

    @Override
    public void setPulse(Channel channel, @Nullable String hex) {
        if (channel == Channel.BOTH || channel == Channel.A) this.aPulse = hex;
        if (channel == Channel.BOTH || channel == Channel.B) this.bPulse = hex;
    }

    @Override
    public void setMaxStrength(Channel channel, int num) {
        if (channel == Channel.BOTH || channel == Channel.A) this.aMaxStrength = num;
        if (channel == Channel.BOTH || channel == Channel.B) this.bMaxStrength = num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientImpl that = (ClientImpl) o;
        return Objects.equals(targetId, that.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetId);
    }
}

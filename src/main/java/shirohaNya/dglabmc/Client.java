package shirohaNya.dglabmc;

import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.enums.AdjustMode;
import shirohaNya.dglabmc.enums.Channel;
import shirohaNya.dglabmc.scripts.Script;
import shirohaNya.dglabmc.scripts.ScriptManager;

import java.util.HashSet;

import static org.bukkit.Bukkit.getLogger;
import static shirohaNya.dglabmc.DGlabMC.*;
import static shirohaNya.dglabmc.utils.DGlabUtils.toDGJson;

@Data
public class Client {
    private final String clientId;
    private final WebSocket webSocket;
    private final BossbarManager bossbar = new BossbarManager();
    private final HashSet<Script> enabledScripts = new HashSet<>();
    @Nullable
    private Player player;
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

    public Client(String clientId, WebSocket webSocket, @Nullable Player player) {
        this.clientId = clientId;
        this.webSocket = webSocket;
        this.player = player;
        clients.add(this);
        enabledScripts.addAll(ScriptManager.getDefaultScripts());
    }

    public void output(String text) {
        getLogger().info("服务器发出: " + text);
        webSocket.send(text);
    }

    public void removeClient() {
        clients.remove(this);
        getLogger().info("已断开" + clientId + "的连接");
        if (player != null) player.sendMessage("你绑定的 " + clientId + " 已断开连接");
        bossbar.removePlayer();
    }

    public void sendMessage(String msg) {
        if (player == null) return;
        player.sendMessage(msg);
    }

    public void unbind() {
        bind(null);
    }

    public void bind(@Nullable Player p) {
        resetBossbarTitle();
        if (player != null) bossbar.removePlayer();
        this.player = p;
        if (p != null) bossbar.addPlayer(p);
    }

    public void sendWave(Channel channel) {
        if ((channel == Channel.BOTH || channel == Channel.A) && aPulse != null) output(toDGJson("msg", mcUUID, clientId, "pulse-A:" + aPulse));
        if ((channel == Channel.BOTH || channel == Channel.B) && bPulse != null) output(toDGJson("msg", mcUUID, clientId, "pulse-B:" + bPulse));
    }

    @Deprecated
    public void sendWave(Channel channel,@Nullable String hex) {
        if (hex == null) {
            clearWave(channel);
            return;
        }
        if (channel == Channel.BOTH) {
            output(toDGJson("msg", mcUUID, clientId, "pulse-A:" + hex));
            output(toDGJson("msg", mcUUID, clientId, "pulse-B:" + hex));
            return;
        }
        output(toDGJson("msg", mcUUID, clientId, "pulse-" + channel.getText() + ":" + hex));
    }

    public void clearWave(Channel channel){
        if (channel == Channel.BOTH) {
            output(toDGJson("msg", mcUUID, clientId, "clear-1"));
            output(toDGJson("msg", mcUUID, clientId, "clear-2"));
            return;
        }
        output(toDGJson("msg", mcUUID, clientId, "clear-" + channel.getValue()));
    }

    public void giveShock(int sec,Channel channel, boolean replace) {
        if (sec == 0) {
            cancelShock(channel);
            return;
        }
        if (channel == Channel.A || channel == Channel.BOTH){
            if (replace) aTicks = 0;
            this.aTotalTime = replace ? sec : Math.max(aTotalTime + sec, 0);
            if (aShockTask == null) this.aShockTask = new BukkitRunnable() {
                @Override
                public void run() {
                    sendWave(Channel.A);
                    resetBossbarTitle();
                    if (aTicks >= aTotalTime * 20) {
                        cancelShock(Channel.A);
                        return;
                    }
                    bossbar.setAProgress(1.0 - (aTicks / (aTotalTime * 20)));
                    aTicks++;
                }
            }.runTaskTimerAsynchronously(plugin, 0, 1);
        }
        if (channel == Channel.B || channel == Channel.BOTH){
            if (replace) bTicks = 0;
            this.bTotalTime = replace ? sec : Math.max(bTotalTime + sec, 0);
            if (bShockTask == null) this.bShockTask = new BukkitRunnable() {
                @Override
                public void run() {
                    sendWave(Channel.B);
                    resetBossbarTitle();
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

    private void cancelShock(Channel channel) {
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
        resetBossbarTitle();
    }

    public void resetBossbarTitle() {
        int aTime = (int) (aTotalTime - aTicks / 20);
        int bTime = (int) (bTotalTime - bTicks / 20);
        bossbar.getABossbar().setTitle("A:" + aStrength + "/" + aMaxStrength +
                " 电击剩余时间:" + aTime + "秒");
        bossbar.getBBossbar().setTitle("B:" + bStrength + "/" + bMaxStrength +
                " 电击剩余时间:" + bTime + "秒");
    }
    //通道: 1 - A 通道；2 - B 通道
    //强度变化模式: 0 - 通道强度减少；1 - 通道强度增加；2 - 通道强度变化为指定数值
    //数值: 范围在(0 ~ 200)的整型
    public void adjustStrength(Channel channel, AdjustMode type, String num){
        if (channel == Channel.BOTH) {
            output(toDGJson("msg", mcUUID, clientId, "strength-1+" + type.getValue() + "+" + num));
            output(toDGJson("msg", mcUUID, clientId, "strength-2+" + type.getValue() + "+" + num));
            return;
        }
        output(toDGJson("msg", mcUUID, clientId, "strength-" + channel.getValue() + "+" + type.getValue() + "+" + num));
    }

    public void adjustStrength(Channel channel, AdjustMode type, int num){
        adjustStrength(channel, type, String.valueOf(num));
    }

    public void adjustPulse(Channel channel,@Nullable String hex){
        if (channel == Channel.BOTH || channel == Channel.A) this.aPulse = hex;
        if (channel == Channel.BOTH || channel == Channel.B) this.bPulse = hex;
    }
}

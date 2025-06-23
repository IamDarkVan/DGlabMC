package shirohaNya.dglabmc;

import shirohaNya.dglabmc.enums.AdjustMode;
import shirohaNya.dglabmc.enums.Channel;
import shirohaNya.dglabmc.scripts.Script;
import lombok.Data;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.scripts.ScriptManager;

import java.util.HashSet;

import static shirohaNya.dglabmc.DGlabMC.*;
import static shirohaNya.dglabmc.utils.DGlabUtils.toDGJson;
import static org.bukkit.Bukkit.createBossBar;
import static org.bukkit.Bukkit.getLogger;

@Data
public class Client {
    private final String clientId;
    private final WebSocket webSocket;
    private final BossBar bossbar = createBossBar(null, BarColor.RED, BarStyle.SOLID);
    private final HashSet<Script> enabledScripts = new HashSet<>();
    @Nullable
    private Player player;
    private Integer aStrength = 0;
    private Integer bStrength = 0;
    private Integer aMaxStrength = 0;
    private Integer bMaxStrength = 0;
    private String aPulse = "[0A0A0A0A00000000,0A0A0A0A0A0A0A0A,0A0A0A0A14141414,0A0A0A0A1E1E1E1E,0A0A0A0A28282828,0A0A0A0A32323232,0A0A0A0A3C3C3C3C,0A0A0A0A46464646,0A0A0A0A50505050,0A0A0A0A5A5A5A5A,0A0A0A0A64646464]";
    private String bPulse = "[0A0A0A0A00000000,0A0A0A0A0A0A0A0A,0A0A0A0A14141414,0A0A0A0A1E1E1E1E,0A0A0A0A28282828,0A0A0A0A32323232,0A0A0A0A3C3C3C3C,0A0A0A0A46464646,0A0A0A0A50505050,0A0A0A0A5A5A5A5A,0A0A0A0A64646464]";
    private double totalTime = 0;
    private int ticks = 0;
    @Nullable
    private BukkitTask shockTask;

    public Client(String clientId, WebSocket webSocket, @Nullable Player player) {
        this.clientId = clientId;
        this.webSocket = webSocket;
        this.player = player;
        clients.add(this);
        bossbar.setProgress(0);
        enabledScripts.addAll(ScriptManager.getDefaultScripts());
    }

    public void output(String text) {
        output(text, true);
    }

    protected void output(String text, boolean log) {
        webSocket.send(text);
        if (log) getLogger().info("服务器发出: " + text);
    }

    public void removeClient() {
        clients.remove(this);
        getLogger().info("已断开" + clientId + "的连接");
        if (player != null) player.sendMessage("你绑定的 " + clientId + " 已断开连接");
        bossbar.removeAll();
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
        if (player != null) bossbar.removePlayer(player);
        this.player = p;
        if (p != null) bossbar.addPlayer(p);
    }

    public void clearPulse(Channel channel){
        if (channel == Channel.BOTH) {
            output(toDGJson("msg", mcUUID, clientId, "clear-1"));
            output(toDGJson("msg", mcUUID, clientId, "clear-2"));
            return;
        }
        output(toDGJson("msg", mcUUID, clientId, "clear-" + channel.getValue()));
    }

    public void giveShock(int sec) {
        giveShock(sec, true);
    }

    public void giveShock(int sec, boolean replace) {
        if (sec == 0) {
            cancelShock();
            return;
        }
        if (replace) ticks = 0;
        this.totalTime = replace ? sec : Math.max(totalTime + sec, 0);
        if (shockTask == null) this.shockTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (aPulse != null) output(toDGJson("msg", mcUUID, clientId, "pulse-A:" + aPulse), false);
                if (bPulse != null) output(toDGJson("msg", mcUUID, clientId, "pulse-B:" + bPulse), false);
                resetBossbarTitle();
                if (ticks >= totalTime * 20) {
                    cancelShock();
                    return;
                }
                bossbar.setProgress(1.0 - (ticks / (totalTime * 20)));
                ticks++;
            }
        }.runTaskTimerAsynchronously(plugin, 0, 1);
    }

    private void cancelShock() {
        clearPulse(Channel.BOTH);
        bossbar.setProgress(0.0);
        totalTime = 0;
        ticks = 0;
        if (shockTask != null) shockTask.cancel();
        shockTask = null;
        resetBossbarTitle();
    }

    public void resetBossbarTitle() {
        bossbar.setTitle("A:" + aStrength + "/" + aMaxStrength +
                " B:" + bStrength + "/" + bMaxStrength +
                " 电击剩余时间:" + (totalTime - ticks / 20) + "秒");
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

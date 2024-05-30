package darkvan.dglabmc;

import darkvan.dglabmc.games.Game;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

import static darkvan.dglabmc.DGlabMC.*;
import static darkvan.dglabmc.utils.DGlabUtils.toDGJson;
import static org.bukkit.Bukkit.createBossBar;
import static org.bukkit.Bukkit.getLogger;

public class Client {
    @Getter private final String clientId;
    @Getter private final WebSocket webSocket;
    @Getter private final BossBar bossbar = createBossBar(null, BarColor.RED, BarStyle.SOLID);
    @Getter @Nullable private Player player;
    @Getter @Setter private Integer aStrength = 0;
    @Getter @Setter private Integer bStrength = 0;
    @Getter @Setter private Integer aMaxStrength = 0;
    @Getter @Setter private Integer bMaxStrength = 0;
    @Getter @Setter private String aPulse = "[0A0A0A0A00000000,0A0A0A0A0A0A0A0A,0A0A0A0A14141414,0A0A0A0A1E1E1E1E,0A0A0A0A28282828,0A0A0A0A32323232,0A0A0A0A3C3C3C3C,0A0A0A0A46464646,0A0A0A0A50505050,0A0A0A0A5A5A5A5A,0A0A0A0A64646464]";
    @Getter @Setter private String bPulse = "[0A0A0A0A00000000,0A0A0A0A0A0A0A0A,0A0A0A0A14141414,0A0A0A0A1E1E1E1E,0A0A0A0A28282828,0A0A0A0A32323232,0A0A0A0A3C3C3C3C,0A0A0A0A46464646,0A0A0A0A50505050,0A0A0A0A5A5A5A5A,0A0A0A0A64646464]";
    @Getter private double totalTime = 0;
    @Getter private int ticks = 0;
    @Getter @Nullable private BukkitTask shockTask;
    private final HashSet<Game> enabledGames = new HashSet<>();

    public Client(String clientId, WebSocket webSocket, @Nullable Player player){
        this.clientId = clientId;
        this.webSocket = webSocket;
        this.player = player;
        clients.add(this);
        bossbar.setProgress(0);
    }
    public void output(String text){
        output(text, true);
    }
    public void output(String text, boolean log){
        webSocket.send(text);
        if (log) getLogger().info("服务器发出: " + text);
    }
    public void removeClient(){
        clients.remove(this);
        getLogger().info("已断开" + clientId + "的连接");
        if (player != null) player.sendMessage("你绑定的 " + clientId + " 已断开连接");
        bossbar.removeAll();
    }
    public String info(){
        return clientId + " " + (player == null ? "未绑定" : player.getName()) +
                " A:" + aStrength + "/" + aMaxStrength +
                " B:" + bStrength + "/" + bMaxStrength +
                " 电击剩余时间:" + (totalTime - ticks / 20) + "秒";
    }

    public void sendMessage(String msg) {
        if (player == null) return;
        player.sendMessage(msg);
    }
    public void bind(@Nullable Player p){
        resetBossbarTitle();
        if (player != null) bossbar.removePlayer(player);
        this.player = p;
        if (p != null) bossbar.addPlayer(p);
    }
    public void giveShock(int sec){
        giveShock(sec,true);
    }

    //type: 加减:true
    public void giveShock(int sec, boolean replace){
        if (sec == 0){
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
        output(toDGJson("msg", mcUUID, clientId, "clear-1"));
        output(toDGJson("msg", mcUUID, clientId, "clear-2"));
        bossbar.setProgress(0.0);
        totalTime = 0;
        ticks = 0;
        if (shockTask != null) shockTask.cancel();
        shockTask = null;
    }

    public void resetBossbarTitle() {
        bossbar.setTitle("A:" + aStrength + "/" +aMaxStrength +
                        " B:" + bStrength + "/" + bMaxStrength +
                        " 电击剩余时间:" + (totalTime - ticks / 20) + "秒");
    }

}

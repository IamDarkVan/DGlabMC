package shirohaNya.dglabmc.api;

import org.bukkit.entity.Player;
import org.java_websocket.WebSocket;
import shirohaNya.dglabmc.client.BossbarManager;
import shirohaNya.dglabmc.enums.AdjustMode;
import shirohaNya.dglabmc.enums.Channel;

import java.util.HashSet;

public interface Client {
    Player getPlayer();

    String getTargetId();

    WebSocket getWebSocket();

    BossbarManager getBossbar();

    HashSet<Script> getEnabledScripts();

    Integer getAStrength();

    Integer getBStrength();

    Integer getAMaxStrength();

    Integer getBMaxStrength();

    String getAPulse();

    String getBPulse();

    double getATotalTime();

    double getBTotalTime();

    int getATicks();

    int getBTicks();

    void output(String txt);

    void bind(Player p);

    void unbind();

    void removeClient();

    void sendMessage(String msg);
    
    void giveShock(Channel c, int sec, boolean r);

    void cancelShock(Channel c);

    void updateBossbarTitle();

    void adjustStrength(Channel c, AdjustMode t, int num);

    void setStrength(Channel c, int num);

    void setPulse(Channel c, String hex);

    void setMaxStrength(Channel c, int num);
}

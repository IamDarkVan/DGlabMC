package shirohaNya.dglabmc.client;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import shirohaNya.dglabmc.enums.BossbarType;

@Getter
public class BossbarManager {
    private final BossBar aBossbar, bBossbar;
    private Player player;
    private BossbarType type;

    public BossbarManager() {
        this.aBossbar = Bukkit.createBossBar(null, BarColor.BLUE, BarStyle.SOLID);
        this.bBossbar = Bukkit.createBossBar(null, BarColor.GREEN, BarStyle.SOLID);
        this.type = BossbarType.BOTH;
        aBossbar.setProgress(0);
        bBossbar.setProgress(0);
    }

    public void resetBossbar() {
        aBossbar.removeAll();
        bBossbar.removeAll();
    }

    public void setPlayer(@NotNull Player p) {
        this.player = p;
        resetBossbar();
        switch (type) {
            case A:
                aBossbar.addPlayer(p);
                break;
            case B:
                bBossbar.addPlayer(p);
                break;
            case BOTH:
                aBossbar.addPlayer(p);
                bBossbar.addPlayer(p);
                break;
            default:
                break;

        }
    }

    public void setBossbarType(BossbarType type) {
        this.type = type;
        if (player == null) return;
        setPlayer(player);
    }

    public void setAProgress(double p) {
        aBossbar.setProgress(p);
    }

    public void setBProgress(double p) {
        bBossbar.setProgress(p);
    }
}

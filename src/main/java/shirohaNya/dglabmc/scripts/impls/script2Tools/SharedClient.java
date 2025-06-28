package shirohaNya.dglabmc.scripts.impls.script2Tools;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import shirohaNya.dglabmc.api.Client;
import shirohaNya.dglabmc.client.ClientImpl;
import shirohaNya.dglabmc.enums.AdjustMode;
import shirohaNya.dglabmc.enums.Channel;

import static shirohaNya.dglabmc.utils.DGlabUtils.toDGJson;

public class SharedClient extends ClientImpl{
    @Getter
    private final Client sourceClient;
    public SharedClient(@NotNull Player player, Client sourceClient) {
        super(null, null, player);
        this.sourceClient = sourceClient;
        updateBossbarTitle();
        getBossbar().resetBossbar();
        getBossbar().setPlayer(player);
        player.sendMessage("你已被绑定共享app");
    }

    @Override
    public String getTargetId() {
        return sourceClient.getTargetId() + "(shared)";
    }

    @Override
    public Integer getAStrength() {
        return sourceClient.getAStrength();
    }

    @Override
    public Integer getBStrength() {
        return sourceClient.getBStrength();
    }

    @Override
    public Integer getAMaxStrength() {
        return sourceClient.getAMaxStrength();
    }

    @Override
    public Integer getBMaxStrength() {
        return sourceClient.getBMaxStrength();
    }

    @Override
    public double getATotalTime() {
        return sourceClient.getATotalTime();
    }

    @Override
    public int getATicks() {
        return sourceClient.getATicks();
    }

    @Override
    public void output(String text) {
        sourceClient.output(text);
    }

    @Override
    public void bind(@NotNull Player p) {
        throw new RuntimeException("不允许重绑定");
    }

    @Override
    public void giveShock(Channel c, int sec, boolean replace) {
        super.giveShock(Channel.B, sec, replace);
    }

    @Override
    public void cancelShock(Channel channel) {
        super.cancelShock(Channel.B);
    }

    @Override
    protected void sendWave(Channel channel) {
        if (getBPulse() != null) output(toDGJson("msg", sourceClient.getPlayerId(), sourceClient.getTargetId(), "pulse-B:" + getBPulse()));
    }

    @Override
    protected void clearWave(Channel channel) {
        output(toDGJson("msg", sourceClient.getPlayerId(), sourceClient.getTargetId(), "clear-2"));
    }

    @Override
    public void updateBossbarTitle() {
        double aTime = getATotalTime() - getATicks() / 20;
        double bTime = getBTotalTime() - getBTicks() / 20;
        getBossbar().setTitle(Channel.A, "A:" + getAStrength() + "/" + getAMaxStrength() + " 电击剩余时间:" + aTime + "秒");
        getBossbar().setTitle(Channel.B, "B:" + getBStrength() + "/" + getBMaxStrength() + " 电击剩余时间:" + bTime + "秒");
    }

    @Override
    public void adjustStrength(Channel c, AdjustMode type, int num) {
        output(toDGJson("msg", sourceClient.getPlayerId(), sourceClient.getTargetId(), "strength-2+" + type.getValue() + "+" + num));
    }

    @Override
    public void setStrength(Channel c, int num) {
        sourceClient.setStrength(Channel.B, num);
    }
    @Override
    public void setMaxStrength(Channel c, int num) {
        sourceClient.setMaxStrength(Channel.B, num);
    }


}

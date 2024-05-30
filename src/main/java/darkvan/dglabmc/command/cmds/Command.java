package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public abstract class Command{
    protected final CommandSender sender;
    protected final String[] args;
    protected final Integer min;
    protected final Integer max;
    protected final String perm;
    protected final String command;
    protected final String usage;
    protected final Integer length;
    public Command(@NotNull String command, @NotNull CommandSender sender,@NotNull String[] args,@Nullable Integer min,@Nullable Integer max,@Nullable String usage, @Nullable String perm) {
        this.command = command;
        this.sender = sender;
        this.args = args;
        this.min = min;
        this.max = max;
        this.usage = usage;
        this.perm = perm;
        this.length = args.length;
    }

    public boolean execute(){
        try {
            checkPermission();
            checkArgsCount();
            errorHandle();
            run();
        } catch (CmdException e){
            sender.sendMessage(e.getMessage());
            return true;
        } catch (IllegalArgumentException e) {
            sender.sendMessage("非法参数");
            return true;
        }catch (Exception ex){
            getLogger().info(sender.getName() + "整幺蛾子了" + ex);
            ex.printStackTrace();
            return true;
        }
        return true;
    }
    private void checkArgsCount() throws CmdException {
        if (!((min == null || length >= min ) && (max == null || length <= max))) throw new CmdException(usage);
    }
    private void checkPermission() throws CmdException {
        if (perm != null && !sender.hasPermission(perm)) throw new CmdException("你没有权限");
    }
    protected abstract void errorHandle() throws CmdException;
    protected abstract void run();
    public abstract List<String> tabComplete();
}

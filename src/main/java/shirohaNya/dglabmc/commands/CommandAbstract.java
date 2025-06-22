package shirohaNya.dglabmc.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;

public abstract class CommandAbstract implements Command{
    protected final CommandSender sender;
    protected final String[] args;
    protected final String[] rawArgs;
    protected final Integer length;
    private final Integer minLength;
    private final Integer maxLength;
    private final String permission;
    private final String name;
    private final String usage;

    public CommandAbstract(@NotNull String name, @NotNull CommandSender sender, @Nullable String[] args, @Nullable Integer minLength, @Nullable Integer maxLength, @Nullable String usage, @Nullable String permission) {
        this.name = name;
        this.sender = sender;
        this.args = args != null ? Arrays.stream(args).filter(Objects::nonNull).map(String::toLowerCase).toArray(String[]::new) : new String[0];
        this.rawArgs = args;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.usage = usage;
        this.permission = permission;
        this.length = this.args.length;
    }

    @Override
    public boolean execute() {
        try {
            checkPermission();
            checkArgsCount();
            errorHandle();
            run();
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
            return true;
        } catch (IllegalArgumentException e) {
            sender.sendMessage("非法参数");
            return true;
        } catch (Exception ex) {
            getLogger().info(sender.getName() + "整幺蛾子了" + ex);
            ex.printStackTrace();
            return true;
        }
        return true;
    }

    private void checkArgsCount() throws CommandException {
        if (!((minLength == null || length >= minLength) && (maxLength == null || length <= maxLength))) throw new CommandException(usage);
    }

    private void checkPermission() throws CommandException {
        if (permission != null && !sender.hasPermission(permission)) throw new CommandException("你没有权限");
    }

    @Override
    public String getCommand(boolean ignorePerm) {
        return (ignorePerm || permission == null || sender.hasPermission(permission)) ? name : null;
    }

    @Override
    public String getUsage(boolean ignorePerm) {
        return (ignorePerm || permission == null || sender.hasPermission(permission)) ? usage : null;
    }

    protected abstract void errorHandle() throws CommandException;

    protected abstract void run();

    public abstract List<String> tabComplete();
}

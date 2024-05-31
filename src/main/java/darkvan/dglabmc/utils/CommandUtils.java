package darkvan.dglabmc.utils;

import darkvan.dglabmc.command.cmds.*;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandUtils {
    private CommandUtils() throws Exception {
        throw new Exception("工具类不允许实例化");
    }

    public static boolean sendHelp(CommandSender sender) {
        sender.sendMessage("---------------------------------");
        usages(sender, false).forEach(sender::sendMessage);
        sender.sendMessage("---------------------------------");
        return true;
    }

    public static List<String> usages(CommandSender sender, boolean ignorePerm) {
        return Stream.of(
                new CommandBind(sender, null).getUsage(ignorePerm),
                new CommandInfo(sender, null).getUsage(ignorePerm),
                new CommandBindList(sender, null).getUsage(ignorePerm),
                new CommandCtrlPulse(sender, null).getUsage(ignorePerm),
                new CommandCtrlStrength(sender, null).getUsage(ignorePerm),
                new CommandGame(sender, null).getUsage(ignorePerm),
                new CommandGameList(sender, null).getUsage(ignorePerm),
                new CommandGetQRCode(sender, null).getUsage(ignorePerm),
                new CommandHelp(sender, null).getUsage(ignorePerm),
                new CommandList(sender, null).getUsage(ignorePerm),
                new CommandReload(sender, null).getUsage(ignorePerm),
                new CommandSendDGJson(sender, null).getUsage(ignorePerm),
                new CommandSendMsg(sender, null).getUsage(ignorePerm),
                new CommandServerRun(sender, null).getUsage(ignorePerm),
                new CommandServerStop(sender, null).getUsage(ignorePerm),
                new CommandShock(sender, null).getUsage(ignorePerm),
                new CommandUnbind(sender, null).getUsage(ignorePerm)
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static List<String> usages(CommandSender sender) {
        return usages(sender, false);
    }

    public static List<String> cmds(CommandSender sender, boolean ignorePerm) {
        return Stream.of(
                new CommandBind(sender, null).getCommand(ignorePerm),
                new CommandInfo(sender, null).getCommand(ignorePerm),
                new CommandBindList(sender, null).getCommand(ignorePerm),
                new CommandCtrlPulse(sender, null).getCommand(ignorePerm),
                new CommandCtrlStrength(sender, null).getCommand(ignorePerm),
                new CommandGame(sender, null).getCommand(ignorePerm),
                new CommandGameList(sender, null).getCommand(ignorePerm),
                new CommandGetQRCode(sender, null).getCommand(ignorePerm),
                new CommandHelp(sender, null).getCommand(ignorePerm),
                new CommandList(sender, null).getCommand(ignorePerm),
                new CommandReload(sender, null).getCommand(ignorePerm),
                new CommandSendDGJson(sender, null).getCommand(ignorePerm),
                new CommandSendMsg(sender, null).getCommand(ignorePerm),
                new CommandServerRun(sender, null).getCommand(ignorePerm),
                new CommandServerStop(sender, null).getCommand(ignorePerm),
                new CommandShock(sender, null).getCommand(ignorePerm),
                new CommandUnbind(sender, null).getCommand(ignorePerm)
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static List<String> cmds(CommandSender sender) {
        return cmds(sender, false);
    }
}

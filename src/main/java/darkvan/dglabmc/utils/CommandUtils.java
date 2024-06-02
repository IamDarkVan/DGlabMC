package darkvan.dglabmc.utils;

import darkvan.dglabmc.Client;
import darkvan.dglabmc.command.cmds.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static darkvan.dglabmc.DGlabMC.clients;
import static org.bukkit.Bukkit.getOnlinePlayers;

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

    public static List<String> commandList(CommandSender sender, boolean ignorePerm) {
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

    public static List<String> commandList(CommandSender sender) {
        return commandList(sender, false);
    }

    public static List<String> getPlayerAndClientList(CommandSender sender) {
        if (sender.hasPermission("dglab.list") && sender.hasPermission("dglab.others"))
            return Stream.concat(getOnlinePlayers().stream().map(Player::getName), clients.stream().map(Client::getClientId)).collect(Collectors.toList());
        if (sender.hasPermission("dglab.others")) return getPlayerList(sender);
        if (sender.hasPermission("dglab.list")) return getClientList(sender);
        return null;
    }
    public static List<String> concatList(List<String> list, String... args) {
        if (list == null) return Arrays.asList(args);
        return Stream.concat(list.stream(), Arrays.stream(args)).collect(Collectors.toList());
    }
    public static List<String> getPlayerList(CommandSender sender) {
        if (sender.hasPermission("dglab.others")) return getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        return null;
    }
    public static List<String> getClientList(CommandSender sender) {
        if (sender.hasPermission("dglab.list")) return clients.stream().map(Client::getClientId).collect(Collectors.toList());
        return null;
    }
}

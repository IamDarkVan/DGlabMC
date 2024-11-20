package darkvan.dglabmc.utils;

import darkvan.dglabmc.Client;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static darkvan.dglabmc.DGlabMC.clients;
import static darkvan.dglabmc.command.CmdManager.getCmdManager;
import static org.bukkit.Bukkit.getOnlinePlayers;

public class CommandUtils {
    private CommandUtils() throws Exception {
        throw new Exception("工具类不允许实例化");
    }

    public static boolean sendHelp(CommandSender sender) {
        sender.sendMessage("---------------------------------");
        getUsageList(sender).forEach(sender::sendMessage);
        sender.sendMessage("---------------------------------");
        return true;
    }
    public static List<String> getUsageList(CommandSender sender, boolean ignorePerm){
        return getCmdManager().getCommandMap().values().stream().map(cmd -> cmd.apply(sender, null).getUsage(ignorePerm)).filter(Objects::nonNull).collect(Collectors.toList());
    }
    public static List<String> getUsageList(CommandSender sender){
        return getUsageList(sender, false);
    }

    public static List<String> getCommandList(CommandSender sender, boolean ignorePerm){
        return getCmdManager().getCommandMap().values().stream().map(cmd -> cmd.apply(sender, null).getCommand(ignorePerm)).filter(Objects::nonNull).collect(Collectors.toList());
    }
    public static List<String> getCommandList(CommandSender sender){
        return getCommandList(sender, false);
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

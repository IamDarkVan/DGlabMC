package darkvan.dglabmc.command;

import darkvan.dglabmc.command.cmds.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static darkvan.dglabmc.utils.CommandUtils.sendHelp;

public class CmdExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) return sendHelp(sender);
        String[] lowerArgs = Arrays.stream(args).map(String::toLowerCase).toArray(String[]::new);
        switch (args[0].toLowerCase()) {
            case "bind":            return new CommandBind(sender, lowerArgs).execute();
            case "info":            return new CommandInfo(sender, lowerArgs).execute();
            case "bind-list":       return new CommandBindList(sender, lowerArgs).execute();
            case "ctrl-pulse":      return new CommandCtrlPulse(sender, lowerArgs).execute();
            case "ctrl-strength":   return new CommandCtrlStrength(sender, lowerArgs).execute();
            case "game":            return new CommandGame(sender,lowerArgs).execute();
            case "game-list":       return new CommandGameList(sender,lowerArgs).execute();
            case "getqrcode":       return new CommandGetQRCode(sender, lowerArgs).execute();
            case "help":            return new CommandHelp(sender, lowerArgs).execute();
            case "list":            return new CommandList(sender, lowerArgs).execute();
            case "reload":          return new CommandReload(sender, lowerArgs).execute();
            case "send-dgjson":     return new CommandSendDGJson(sender, args).execute();
            case "send-msg":        return new CommandSendMsg(sender, args).execute();
            case "server-run":      return new CommandServerRun(sender, lowerArgs).execute();
            case "server-stop":     return new CommandServerStop(sender, lowerArgs).execute();
            case "shock":           return new CommandShock(sender, lowerArgs).execute();
            case "unbind":          return new CommandUnbind(sender, lowerArgs).execute();
            default:                return sendHelp(sender);
        }
    }

}

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
            case "bind" ->          {return new CommandBind(sender, lowerArgs, null).execute();}
            case "info" ->          {return new CommandInfo(sender, lowerArgs,  null).execute();}
            case "bind-list" ->     {return new CommandBindList(sender, lowerArgs, null).execute();}
            case "ctrl-pulse" ->    {return new CommandCtrlPulse(sender, lowerArgs,  null).execute();}
            case "ctrl-strength" -> {return new CommandCtrlStrength(sender, lowerArgs, null).execute();}
            case "getqrcode" ->     {return new CommandGetQRCode(sender, lowerArgs, null).execute();}
            case "help" ->          {return new CommandHelp(sender, lowerArgs, null).execute();}
            case "list" ->          {return new CommandList(sender, lowerArgs,  null).execute();}
            case "reload" ->        {return new CommandReload(sender, lowerArgs, null).execute();}
            case "send-dgjson" ->   {return new CommandSendDGJson(sender, args, null).execute();}
            case "send-msg" ->      {return new CommandSendMsg(sender, args,  null).execute();}
            case "server-run" ->    {return new CommandServerRun(sender, lowerArgs,  null).execute();}
            case "server-stop" ->   {return new CommandServerStop(sender, lowerArgs,  null).execute();}
            case "shock" ->         {return new CommandShock(sender, lowerArgs, null).execute();}
            case "unbind" ->        {return new CommandUnbind(sender, lowerArgs,  null).execute();}
        }
        return sendHelp(sender);
    }

}

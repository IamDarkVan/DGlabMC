package darkvan.dglabmc.command;

import darkvan.dglabmc.command.cmds.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static darkvan.dglabmc.utils.CommandUtils.cmds;

public class CmdTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args){
        if (args.length == 1) return cmds();
        String[] lowerArgs = Arrays.stream(args).map(String::toLowerCase).toArray(String[]::new);
        switch(lowerArgs[0]){
            case "bind" ->          {return new CommandBind(sender, lowerArgs, null).tabComplete();}
            case "bind-get" ->      {return new CommandInfo(sender, lowerArgs,  null).tabComplete();}
            case "bind-list" ->     {return new CommandBindList(sender, lowerArgs, null).tabComplete();}
            case "ctrl-pulse" ->    {return new CommandCtrlPulse(sender, lowerArgs,  null).tabComplete();}
            case "ctrl-strength" -> {return new CommandCtrlStrength(sender, lowerArgs, null).tabComplete();}
            case "getqrcode" ->     {return new CommandGetQRCode(sender, lowerArgs, null).tabComplete();}
            case "help" ->          {return new CommandHelp(sender, lowerArgs, null).tabComplete();}
            case "list" ->          {return new CommandList(sender, lowerArgs,  null).tabComplete();}
            case "reload" ->        {return new CommandReload(sender, lowerArgs, null).tabComplete();}
            case "send-dgjson" ->   {return new CommandSendDGJson(sender, args, null).tabComplete();}
            case "send-msg" ->      {return new CommandSendMsg(sender, args,  null).tabComplete();}
            case "server-run" ->    {return new CommandServerRun(sender, lowerArgs,  null).tabComplete();}
            case "server-stop" ->   {return new CommandServerStop(sender, lowerArgs,  null).tabComplete();}
            case "shock" ->         {return new CommandShock(sender, lowerArgs, null).tabComplete();}
            case "unbind" ->        {return new CommandUnbind(sender, lowerArgs,  null).tabComplete();}
        }
        return null;
    }
}

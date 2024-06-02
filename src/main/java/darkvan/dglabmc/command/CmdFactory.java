package darkvan.dglabmc.command;

import darkvan.dglabmc.command.cmds.*;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class CmdFactory {
    private static final Map<String, BiFunction<CommandSender, String[], Command>> commandMap = new HashMap<>();
    static {
        commandMap.put("bind", CommandBind::new);
        commandMap.put("info", CommandInfo::new);
        commandMap.put("bind-list", CommandBindList::new);
        commandMap.put("ctrl-pulse", CommandCtrlPulse::new);
        commandMap.put("ctrl-strength", CommandCtrlStrength::new);
        commandMap.put("game", CommandGame::new);
        commandMap.put("game-list", CommandGameList::new);
        commandMap.put("getqrcode", CommandGetQRCode::new);
        commandMap.put("help", CommandHelp::new);
        commandMap.put("list", CommandList::new);
        commandMap.put("reload", CommandReload::new);
        commandMap.put("send-dgjson", CommandSendDGJson::new);
        commandMap.put("send-msg", CommandSendMsg::new);
        commandMap.put("server-run", CommandServerRun::new);
        commandMap.put("server-stop", CommandServerStop::new);
        commandMap.put("shock", CommandShock::new);
        commandMap.put("unbind", CommandUnbind::new);
    }
    private final Command command;
    public CmdFactory(@NotNull String name, @NotNull CommandSender sender, @NotNull String[] args) {
        this.command = commandMap.containsKey(name) ? commandMap.get(name).apply(sender, args) : commandMap.get("help").apply(sender, args);
    }

    public boolean execute(){
        return command.execute();
    }

    public List<String> tabComplete(){
        return command.tabComplete();
    }
}

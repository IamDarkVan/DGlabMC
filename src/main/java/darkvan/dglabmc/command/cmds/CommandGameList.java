package darkvan.dglabmc.command.cmds;

import darkvan.dglabmc.command.CmdException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static darkvan.dglabmc.games.Game.games;

public class CommandGameList extends Command{

    public CommandGameList(@NotNull CommandSender sender, @NotNull String[] args) {
        super("game-list", sender, args, null, null, "/dglab game-list", "dglab.game.list");
    }

    @Override
    protected void errorHandle() throws CmdException {

    }

    @Override
    protected void run() {
        games.values().forEach(game -> sender.sendMessage(game.getName() + " " + game.getDescription()));
    }

    @Override
    public List<String> tabComplete() {
        return null;
    }
}

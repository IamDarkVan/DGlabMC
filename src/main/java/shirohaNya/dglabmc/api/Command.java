package shirohaNya.dglabmc.api;

import java.util.List;

public interface Command {
    boolean execute();

    String getCommand(boolean ignorePerm);

    String getUsage(boolean ignorePerm);

    List<String> tabComplete();
}

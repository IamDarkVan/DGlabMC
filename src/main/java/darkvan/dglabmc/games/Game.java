package darkvan.dglabmc.games;

import darkvan.dglabmc.Client;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public abstract class Game {
    @Getter private final String name;
    @Getter private final boolean enabled;
    @Getter @Nullable private final String description;
    @Getter @Nullable private final String permission;
    public static final HashMap<Class<? extends Game>,Game> games = new HashMap<>();

    protected Game(String name, boolean enabled, @Nullable String description, @Nullable String permission ){
        this.name = name;
        this.enabled = enabled;
        this.description = description;
        this.permission = permission;
    }

    @SneakyThrows
    public static Game getGame(Class<? extends Game> clazz){
        if (!games.containsKey(clazz)) games.put(clazz, clazz.getDeclaredConstructor().newInstance());
        return games.get(clazz);
    }
    public abstract void onEnable(Client client);
    public abstract void onDisable(Client client);

}

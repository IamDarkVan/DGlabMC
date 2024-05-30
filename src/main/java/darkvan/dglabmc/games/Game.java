package darkvan.dglabmc.games;

import darkvan.dglabmc.Client;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static darkvan.dglabmc.DGlabMC.clients;

public abstract class Game {
    @Getter private final String name;
    @Getter @Nullable private final String description;
    @Getter @Nullable private final String permission;
    @Getter private final Map<String, Object> settings;
    @Getter private final HashSet<Client> enabledClients = new HashSet<>();
    public static final HashMap<Class<? extends Game>,Game> games = new HashMap<>();

    protected Game(String name, @Nullable String description, @Nullable String permission,@Nullable ConfigurationSection configSection){
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.settings = configSection == null ? new HashMap<>() : configSection.getValues(false);
        if (settings.containsKey("default") && settings.get("default") instanceof Boolean def && def) enabledClients.addAll(clients);
    }
    @SneakyThrows
    public static Game getGame(Class<? extends Game> clazz){
        if (!games.containsKey(clazz)) games.put(clazz, clazz.getDeclaredConstructor().newInstance());
        return games.get(clazz);
    }
    public boolean isClientEnabled(Client client){
        return enabledClients.contains(client);
    }
    public void enableClient(Client client){
        enabledClients.add(client);
        onEnable(client);
    }
    public void disableClient(Client client){
        if (!enabledClients.contains(client)) return;
        enabledClients.remove(client);
        onDisable(client);
    }
    public abstract void onEnable(Client client);
    public abstract void onDisable(Client client);


}

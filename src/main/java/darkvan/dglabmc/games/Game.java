package darkvan.dglabmc.games;

import darkvan.dglabmc.Client;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static darkvan.dglabmc.DGlabMC.clients;
import static darkvan.dglabmc.DGlabMC.plugin;

public abstract class Game {
    public static final Map<String, Game> games = new HashMap<>();
    //["game1": {"default": false, "time": 5, "replace": true},"game2": {...}, ...]
    public static final List<Map<?, ?>> gameConfigs = plugin.config.getMapList("games");
    @Getter
    private final String name;
    @Getter
    @Nullable
    private final String description;
    @Getter
    @Nullable
    private final String permission;
    @Getter
    private final Map<?, ?> settings;
    @Getter
    private final HashSet<Client> enabledClients = new HashSet<>();

    protected Game(String name, @Nullable String description, @Nullable String permission) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.settings = gameConfigs.stream().filter(game -> game.containsKey(name)).findFirst().orElse(null);
        if (settings == null) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            throw new RuntimeException("未在config.yml中注册游戏");
        }
        games.put(name, this);
        if (settings.containsKey("default") && settings.get("default") instanceof Boolean bool && bool)
            enabledClients.addAll(clients);
    }

    public static @Nullable Game getGame(String name) {
        return games.get(name);
    }

    public boolean isClientEnabled(Client client) {
        return enabledClients.contains(client);
    }

    public void enableClient(Client client) {
        enabledClients.add(client);
        onEnable(client);
    }

    public void disableClient(Client client) {
        if (!enabledClients.contains(client)) return;
        enabledClients.remove(client);
        onDisable(client);
    }

    public void toggleClient(Client client) {
        if (isClientEnabled(client)) disableClient(client);
        else enableClient(client);
    }

    public abstract void onEnable(Client client);

    public abstract void onDisable(Client client);

}

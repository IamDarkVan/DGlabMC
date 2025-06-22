package shirohaNya.dglabmc.scripts;

import shirohaNya.dglabmc.Client;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static shirohaNya.dglabmc.DGlabMC.*;

@Getter
public abstract class ScriptAbstract implements Script{

    //["script1": {"default": false, "time": 5, "replace": true},"script2": {...}, ...]
    public static final List<Map<?, ?>> scriptConfigs = plugin.config.getMapList("scripts");
    private final String name;
    @Nullable
    private final String description;
    @Nullable
    private final String permission;
    private final Map<?, ?> settings;
    private final HashSet<Client> enabledClients = new HashSet<>();

    protected ScriptAbstract(String name, @Nullable String description, @Nullable String permission) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.settings = scriptConfigs.stream().filter(script -> script.containsKey(name)).findFirst().orElse(null);
        if (settings == null) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            throw new RuntimeException("未在config.yml中注册游戏");
        }
        if (settings.containsKey("default") && settings.get("default") instanceof Boolean && (Boolean) settings.get("default"))
            enabledClients.addAll(clients);
    }

    @Override
    public boolean isClientEnabled(Client client) {
        return enabledClients.contains(client);
    }

    @Override
    public void enableClient(Client client) {
        enabledClients.add(client);
        onEnable(client);
    }

    @Override
    public void disableClient(Client client) {
        if (!enabledClients.contains(client)) return;
        enabledClients.remove(client);
        onDisable(client);
    }

    @Override
    public void toggleClient(Client client) {
        if (isClientEnabled(client)) disableClient(client);
        else enableClient(client);
    }

    public abstract void onEnable(Client client);

    public abstract void onDisable(Client client);

}

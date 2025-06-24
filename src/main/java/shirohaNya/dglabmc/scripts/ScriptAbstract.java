package shirohaNya.dglabmc.scripts;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.Client;

import java.util.Map;

import static shirohaNya.dglabmc.DGlabMC.plugin;

@Getter
public abstract class ScriptAbstract implements Script {


    private final String name;
    @Nullable
    private final String description;
    @Nullable
    private final String permission;
    private final Map<?, ?> settings;

    public ScriptAbstract(String name, @Nullable String description, @Nullable String permission) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.settings = ScriptManager.getConfig(name);
        if (settings == null) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            throw new RuntimeException("未在config.yml中注册游戏");
        }
        if (settings.containsKey("default") && settings.get("default") instanceof Boolean && (Boolean) settings.get("default")) {
            ScriptManager.putDefaultScript(this);
        }
    }

    @Override
    public boolean isClientEnabled(Client client) {
        return client.getEnabledScripts().contains(this);
    }

    @Override
    public void enableClient(Client client) {
        client.getEnabledScripts().add(this);
        onEnable(client);
    }

    @Override
    public void disableClient(Client client) {
        if (!client.getEnabledScripts().contains(this)) return;
        client.getEnabledScripts().remove(this);
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

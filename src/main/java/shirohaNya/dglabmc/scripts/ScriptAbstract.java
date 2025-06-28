package shirohaNya.dglabmc.scripts;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shirohaNya.dglabmc.api.Client;
import shirohaNya.dglabmc.api.Script;

@Getter
public abstract class ScriptAbstract implements Script {
    private final String name;
    @Nullable
    private final String description;
    @Nullable
    private final String permission;
    private final @NotNull ConfigurationSection settings;

    public ScriptAbstract(String name, @Nullable String description, @Nullable String permission) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.settings = ScriptManager.getScriptConfig(name);
        if (settings.getBoolean("default")) {
            ScriptManager.putDefaultScript(this);
        }
    }

    @Override
    public boolean isClientEnabled(Client client) {
        return client.getEnabledScripts().contains(this);
    }

    @Override
    public void enableClient(Client client) {
        if (client.getEnabledScripts().contains(this)) return;
        if (onEnable(client)) client.getEnabledScripts().add(this);
        else client.sendMessage("你现在不能启用该脚本");
    }

    @Override
    public void disableClient(Client client) {
        if (!client.getEnabledScripts().contains(this)) return;
        if (onDisable(client)) client.getEnabledScripts().remove(this);
        else client.sendMessage("你现在不能禁用该脚本");
    }

    @Override
    public void toggleClient(Client client) {
        if (isClientEnabled(client)) disableClient(client);
        else enableClient(client);
    }

    public abstract boolean onEnable(Client client);

    public abstract boolean onDisable(Client client);

}

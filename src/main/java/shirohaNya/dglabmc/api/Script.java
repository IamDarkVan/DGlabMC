package shirohaNya.dglabmc.api;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

@SuppressWarnings("unused")
public interface Script {
    boolean isClientEnabled(Client client);

    void enableClient(Client client);

    void disableClient(Client client);

    void toggleClient(Client client);

    boolean onEnable(Client client);

    boolean onDisable(Client client);

    String getName();

    String getDescription();

    String getPermission();

    ConfigurationSection getSettings();
}

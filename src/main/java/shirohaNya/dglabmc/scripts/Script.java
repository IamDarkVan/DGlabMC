package shirohaNya.dglabmc.scripts;

import shirohaNya.dglabmc.Client;

import java.util.Map;

@SuppressWarnings("unused")
public interface Script {
    boolean isClientEnabled(Client client);

    void enableClient(Client client);

    void disableClient(Client client);

    void toggleClient(Client client);

    void onEnable(Client client);

    void onDisable(Client client);

    String getName();

    String getDescription();

    String getPermission();

    Map<?,?> getSettings();
}

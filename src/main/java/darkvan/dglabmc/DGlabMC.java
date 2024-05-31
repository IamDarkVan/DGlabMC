package darkvan.dglabmc;

import darkvan.dglabmc.command.CmdExecutor;
import darkvan.dglabmc.command.CmdTabCompleter;
import darkvan.dglabmc.listeners.ListenerGame1;
import darkvan.dglabmc.listeners.ListenerUnbindOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import static darkvan.dglabmc.utils.DGlabUtils.runWebSocketServer;

public final class DGlabMC extends JavaPlugin {
    public static DGlabMC plugin;
    public static HashSet<Client> clients = new HashSet<>();
    public static String mcUUID;
    public FileConfiguration config = getConfig();
    public String ip = config.getString("ip");
    public int port = config.getInt("port");
    public boolean autoRunServer = config.getBoolean("autoRunServer");
    public MCWebSocketServer mcWebSocketServer = null;
    public String qrCode;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getLogger().info("Dglab in Minecraft 已加载,服务器使用WebSocket协议,请使用郊狼3.0以上版本主机连接");
        Objects.requireNonNull(getCommand("dglab")).setExecutor(new CmdExecutor());
        Objects.requireNonNull(getCommand("dglab")).setTabCompleter(new CmdTabCompleter());
        Bukkit.getPluginManager().registerEvents(new ListenerUnbindOfflinePlayer(), this);
        Bukkit.getPluginManager().registerEvents(new ListenerGame1(), this);
        saveDefaultConfig();
        mcUUID = config.getString("mcUUID") == null ? UUID.randomUUID().toString() : config.getString("mcUUID");
        qrCode = "https://www.dungeon-lab.com/app-download.php#DGLAB-SOCKET#" + "ws://" + ip + ":" + port + "/" + mcUUID;
        getLogger().info("本机UUID为:" + mcUUID);
        getLogger().info("本机生成二维码为: " + qrCode);
        if (autoRunServer) runWebSocketServer(port);
        else getLogger().info("请使用/dglab server-run启动WebSocket服务器");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveDefaultConfig();
        getLogger().info("已退出插件");
    }
}

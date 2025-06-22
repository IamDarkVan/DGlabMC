package darkvan.dglabmc;

import darkvan.dglabmc.commands.CommandExecutor;
import darkvan.dglabmc.commands.CommandTabCompleter;
import darkvan.dglabmc.commands.impls.*;
import darkvan.dglabmc.listeners.ListenerScript1;
import darkvan.dglabmc.listeners.ListenerUnbindOfflinePlayer;
import darkvan.dglabmc.scripts.ScriptAbstract;
import darkvan.dglabmc.scripts.impls.Script1;
import darkvan.dglabmc.utils.DGlabUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static darkvan.dglabmc.commands.CommandManager.getCmdManager;
import static darkvan.dglabmc.scripts.ScriptManager.getScriptManager;


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
    public static final Map<String, ScriptAbstract> scripts = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getLogger().info("Dglab in Minecraft 已加载,服务器使用WebSocket协议,请使用郊狼3.0以上版本主机连接");
        Objects.requireNonNull(getCommand("dglab")).setExecutor(new CommandExecutor());
        Objects.requireNonNull(getCommand("dglab")).setTabCompleter(new CommandTabCompleter());
        Bukkit.getPluginManager().registerEvents(new ListenerUnbindOfflinePlayer(), this);
        Bukkit.getPluginManager().registerEvents(new ListenerScript1(), this);
        // 注册子命令
        getCmdManager().registerCommand("bind", CommandBind::new);
        getCmdManager().registerCommand("info", CommandInfo::new);
        getCmdManager().registerCommand("bind-list", CommandBindList::new);
        getCmdManager().registerCommand("ctrl-pulse", CommandCtrlPulse::new);
        getCmdManager().registerCommand("ctrl-strength", CommandCtrlStrength::new);
        getCmdManager().registerCommand("script", CommandScript::new);
        getCmdManager().registerCommand("script-list", CommandScriptList::new);
        getCmdManager().registerCommand("getqrcode", CommandGetQRCode::new);
        getCmdManager().registerCommand("help", CommandHelp::new);
        getCmdManager().registerCommand("list", CommandList::new);
        getCmdManager().registerCommand("reload", CommandReload::new);
        getCmdManager().registerCommand("send-dgjson", CommandSendDGJson::new);
        getCmdManager().registerCommand("send-msg", CommandSendMsg::new);
        getCmdManager().registerCommand("server-run", CommandServerRun::new);
        getCmdManager().registerCommand("server-stop", CommandServerStop::new);
        getCmdManager().registerCommand("shock", CommandShock::new);
        getCmdManager().registerCommand("unbind", CommandUnbind::new);
        //注册脚本
        getScriptManager().registerScript("script1", new Script1());
        //生成二维码
        saveDefaultConfig();
        mcUUID = config.getString("mcUUID") == null ? UUID.randomUUID().toString() : config.getString("mcUUID");
        qrCode = "https://www.dungeon-lab.com/app-download.php#DGLAB-SOCKET#" + "ws://" + ip + ":" + port + "/" + mcUUID;
        getLogger().info("本机UUID为:" + mcUUID);
        getLogger().info("本机生成二维码为: " + qrCode);
        if (autoRunServer) DGlabUtils.runWebSocketServer(port);
        else getLogger().info("请使用/dglab server-run启动WebSocket服务器");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveDefaultConfig();
        getLogger().info("已退出插件");
    }
}

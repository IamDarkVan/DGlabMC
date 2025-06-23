package shirohaNya.dglabmc;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import shirohaNya.dglabmc.commands.CommandExecutor;
import shirohaNya.dglabmc.commands.CommandTabCompleter;
import shirohaNya.dglabmc.commands.impls.*;
import shirohaNya.dglabmc.listeners.ListenerScript1;
import shirohaNya.dglabmc.listeners.ListenerUnbindOfflinePlayer;
import shirohaNya.dglabmc.scripts.impls.Script1;
import shirohaNya.dglabmc.utils.DGlabUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import static shirohaNya.dglabmc.commands.CommandManager.registerCommand;
import static shirohaNya.dglabmc.scripts.ScriptManager.registerScript;


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
        Objects.requireNonNull(getCommand("dglab")).setExecutor(new CommandExecutor());
        Objects.requireNonNull(getCommand("dglab")).setTabCompleter(new CommandTabCompleter());
        //注册脚本
        registerScript("script1", new Script1());
        //注册监听器
        Bukkit.getPluginManager().registerEvents(new ListenerUnbindOfflinePlayer(), this);
        Bukkit.getPluginManager().registerEvents(new ListenerScript1(), this);
        // 注册子命令
        registerCommand("bind", CommandBind::new);
        registerCommand("info", CommandInfo::new);
        registerCommand("bind-list", CommandBindList::new);
        registerCommand("ctrl-pulse", CommandCtrlPulse::new);
        registerCommand("ctrl-strength", CommandCtrlStrength::new);
        registerCommand("script", CommandScript::new);
        registerCommand("script-list", CommandScriptList::new);
        registerCommand("getaddress", CommandGetAddress::new);
        registerCommand("getqrcode", CommandGetQRCode::new);
        registerCommand("help", CommandHelp::new);
        registerCommand("list", CommandList::new);
        registerCommand("reload", CommandReload::new);
        registerCommand("send-dgjson", CommandSendDGJson::new);
        registerCommand("send-msg", CommandSendMsg::new);
        registerCommand("server-run", CommandServerRun::new);
        registerCommand("server-stop", CommandServerStop::new);
        registerCommand("shock", CommandShock::new);
        registerCommand("unbind", CommandUnbind::new);
        registerCommand("bossbar", CommandBossbar::new);
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

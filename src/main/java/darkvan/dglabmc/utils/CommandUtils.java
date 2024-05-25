package darkvan.dglabmc.utils;

import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandUtils {
    public static boolean sendHelp(CommandSender sender){
        sender.sendMessage(
                "---------------------------------",
                "以下命令[player]不填默认自己 [clientId]不填默认自己已绑定app",
                "/dglab help -- 显示该页面",
                "/dglab list -- 显示app列表",
                "/dglab getQRCode -- 获取二维码",
                "/dglab reload -- 重载配置文件",
                "/dglab bind-list -- 查询绑定app列表",
                "/dglab info [clientId|player] -- 查询app信息",
                "/dglab bind <clientId> [player] -- 玩家绑定app 使用ctrl-指令不需要clientId",
                "/dglab unbind [clientId|player] --解除玩家绑定app 默认自己",
                "/dglab ctrl-strength [clientId|player] (A|B|both) (add|dec|set) <value> -- 控制强度 (通道 模式 数值)",
                "/dglab ctrl-pulse [clientId|player] (A|B|both) (<HEX[]>|clear) -- 控制波形 (通道 8字节的HEX数组)",
                "/dglab shock [clientId|player] <time(sec)> -- 按照波形放电",
                "/dglab server-run [port] -- 启动WebSocket服务器 不填端口默认config",
                "/dglab server-stop -- 关闭目前WebSocket服务器",
                "/dglab send-msg <clientId> <message> -- 直接向app发送消息(可空格 不推荐使用)",
                "/dglab send-dgjson <clientId> <typ> <cid> <tid> <msg>-- 直接向app发送DGJson(不推荐使用)",
                "---------------------------------"
        );
        return true;
    }

    public static List<String> cmds(){
        return List.of("help", "list", "getqrcode", "reload", "bind-list", "info", "bind", "unbind", "ctrl-strength", "ctrl-pulse", "shock", "server-run", "server-stop", "send-msg", "send-dgjson");
    }
}

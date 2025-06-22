
# DGlabMC Plugin
* 不会写readme就让GPT帮我写了(
# 开发版本:1.19.4
# 理论支持版本:1.9+
## 安装与配置
* 下载并安装插件
* 将DGlabMC插件文件放入服务器的plugins文件夹中。
* 配置IP和端口
* 在config.yml文件中设置您的外网IP地址和端口
* 默认情况下，WebSocket服务器将在端口9999上启动。
* 插件将在plugins/DGlabMC目录下生成一个二维码文件。
* 请使用您的应用程序扫描此二维码进行绑定。

## 使用指南
* 玩家在进入服务器后，可以通过以下步骤进行操作：
* 1.绑定应用程序
* 使用/dglab bind命令绑定您的应用程序。
* 2.控制波形
* 使用/dglab ctrl-pulse命令控制波形。
* 3.控制强度
* 使用/dglab ctrl-strength命令控制强度。
* 4.放电
* 使用/dglab shock <秒数>命令进行放电。

## 命令列表
* 以下是插件支持的命令及其功能说明：
* /dglab help -- 显示帮助页面。
* /dglab list -- 显示已绑定的应用程序列表。
* /dglab getQRCode -- 获取二维码。
* /dglab reload -- 重载配置文件。
* /dglab bind-list -- 查询绑定的应用程序列表。
* /dglab bind-get [clientId|player] -- 查询指定玩家或应用程序的绑定信息。
* /dglab bind <clientId> [player] -- 绑定玩家和应用程序。使用ctrl-命令时不需要提供clientId。
* /dglab unbind [clientId|player] -- 解除玩家和应用程序的绑定，默认解除自己。
* /dglab ctrl-strength [clientId|player] (A|B|both) (add|dec|set) <value> -- 控制强度，设置通道、模式和数值。
* /dglab ctrl-pulse [clientId|player] (A|B|both) (<HEX[]>|clear) -- 控制波形，设置通道和8字节的HEX数组。
* /dglab shock [clientId|player] <time(sec)> -- 按照波形放电。
* /dglab server-run [port] -- 启动WebSocket服务器，不填端口则使用配置文件中的默认端口。
* /dglab server-stop -- 关闭当前的WebSocket服务器。
* /dglab send-msg <clientId> <message> -- 直接向应用程序发送消息（可使用空格，不推荐使用）。
* /dglab send-dgjson <clientId> <typ> <cid> <tid> <msg> -- 直接向应用程序发送DGJson（不推荐使用）。

## 注意事项
* 请确保在config.yml中正确设置IP地址和端口，以便WebSocket服务器能够正常运行。
* 在玩家首次进入服务器后，请指导其使用/dglab bind命令进行应用程序绑定。
* 使用控制命令如/dglab ctrl-pulse和/dglab ctrl-strength时，请确保输入的参数正确，以免造成误操作。
* 如有任何问题，请参考插件的帮助命令/dglab help或联系插件开发者。

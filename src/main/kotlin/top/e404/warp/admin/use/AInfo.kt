package top.e404.warp.admin.use

import org.bukkit.command.CommandSender
import top.e404.warp.command.AbstractCommand
import top.e404.warp.util.Log.color
import top.e404.warp.util.sendMsgWithPrefix
import top.e404.warp.warp.WarpManager
import top.e404.warp.warp.addAllWarp

object AInfo : AbstractCommand(
    "info",
    false,
    "ewarp.admin",
) {
    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        // 检查参数长度
        if (errorArgsLength(args, 2, sender)) return
        val name = args[1]
        val target = WarpManager.warps[name]
        // 不存在的warp
        if (target == null) {
            sender.sendMsgWithPrefix("&c你还未创建名为&6${name}&c的warp")
            return
        }
        // 展示信息
        target.fullInfo(sender)
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
        if (args.size == 2) complete.addAllWarp()
    }

    override fun help() = "&a/ewa info <名字>&f - 查看warp详细信息".color()
}
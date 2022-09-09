package top.e404.warp.command.use

import org.bukkit.command.CommandSender
import top.e404.warp.command.AbstractCommand
import top.e404.warp.util.Log.color
import top.e404.warp.util.sendMsgWithPrefix
import top.e404.warp.warp.WarpManager

/**
 * 列出所有warp
 */
object ListAll : AbstractCommand(
    "listall",
    true,
    "ewarp.use",
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 1, sender)) return
        // 可用warp
        val canUse = WarpManager.getCanUse(sender).keys
        if (canUse.isEmpty()) {
            sender.sendMsgWithPrefix("&c没有可用的warp")
            return
        }
        sender.sendMsgWithPrefix("&a当前可用warp: &7[&6${canUse.joinToString("&7, &6")}&7]")
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
    }

    override fun help() = "&a/ew listall&f - 查看所有可见的warp".color()
}
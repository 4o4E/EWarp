package top.e404.warp.admin.use

import org.bukkit.command.CommandSender
import top.e404.warp.command.AbstractCommand
import top.e404.warp.util.Log.color
import top.e404.warp.warp.every
import top.e404.warp.warp.limit
import top.e404.warp.warp.start

object ALimit : AbstractCommand(
    "limit",
    false,
    "ewarp.admin",
) {
    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        sender.sendMessage(
            """&f-=[ &6限制查询 &f]=-
            |&f默认warp上限${limit()}个
            |&f初始价格${start()}
            |&f后续价格${every()}"""
                .trimMargin()
                .color()
        )
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
    }

    override fun help() = "&a/ewa limit&f - 查询创建限制和价格".color()
}
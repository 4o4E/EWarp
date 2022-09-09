package top.e404.warp.admin.use

import org.bukkit.command.CommandSender
import top.e404.warp.command.AbstractCommand
import top.e404.warp.util.Log.color
import top.e404.warp.util.sendMsgWithPrefix
import top.e404.warp.warp.WarpManager

object AList : AbstractCommand(
    "list",
    false,
    "ewarp.admin",
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 2, sender)) return
        val name = args[1]
        val list = WarpManager.getOwner(name).keys
        if (list.isEmpty()) {
            sender.sendMsgWithPrefix("&c玩家&2${name}&c还没有创建过warp")
            return
        }
        sender.sendMsgWithPrefix("&f玩家&2${name}&f创建的warp有: &7[&6${list.joinToString("&7, &6")}&7]")
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
        if (args.size == 2) complete.addAll(WarpManager.warps
            .map { it.value.owner }
            .groupBy { it }.keys
        )
    }

    override fun help() = "&a/ewa list <玩家名字>&f - 查看该玩家的warp".color()
}
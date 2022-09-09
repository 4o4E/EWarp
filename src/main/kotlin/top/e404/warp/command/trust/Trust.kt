package top.e404.warp.command.trust

import org.bukkit.command.CommandSender
import top.e404.warp.command.AbstractCommand
import top.e404.warp.util.Log.color
import top.e404.warp.util.sendMsgWithPrefix
import top.e404.warp.warp.WarpManager
import top.e404.warp.warp.addOnlineWithoutSelf
import top.e404.warp.warp.addOwner

object Trust : AbstractCommand(
    "trust",
    true,
    "ewarp.use",
    "ewarp.trust",
    "ewarp.trust"
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 3, sender)) return
        val name = args[1]
        val trust = args[2]
        val target = WarpManager.getCanEdit(sender)[name]
        // 不存在的warp
        if (target == null) {
            sender.sendMsgWithPrefix("&c你还未创建名为&6${name}&c的warp")
            return
        }
        // 已有此信任者
        if (target.trusts.any { it.equals(trust, true) }) {
            sender.sendMsgWithPrefix("&6${name}&c的信任者列表中已存在玩家${trust}")
            return
        }
        // 修改
        target.trusts.add(trust)
        sender.sendMsgWithPrefix(
            """&a成功在&6${name}&a中添加信任者${trust}
            |&a当前信任者有: &7[&6${target.trusts.joinToString("&7, &6")}&7]""".trimMargin()
        )
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>, complete: MutableList<String>) {
        when (args.size) {
            2 -> complete.addOwner(sender)
            3 -> complete.addOnlineWithoutSelf(sender)
        }
    }

    override fun help() = """&a/ew trust <warp名字> <信任者名字>&f - 添加信任者
        |  &f信任者可用访问你设置为私有的warp""".trimMargin().color()
}
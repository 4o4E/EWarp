package top.e404.warp.admin.edit

import org.bukkit.command.CommandSender
import top.e404.warp.command.AbstractCommand
import top.e404.warp.util.Log.color
import top.e404.warp.util.sendMsgWithPrefix
import top.e404.warp.warp.WarpManager
import top.e404.warp.warp.addAllWarp

object APrivate : AbstractCommand(
    "private",
    false,
    "ewarp.admin",
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 2, sender)) return
        val name = args[1]
        val target = WarpManager.warps[name]
        // 不存在的warp
        if (target == null) {
            sender.sendMsgWithPrefix("&c不存在名为&6${name}&c的warp")
            return
        }
        // 无效修改
        if (!target.public) {
            sender.sendMsgWithPrefix("&7此warp已是私有状态")
            return
        }
        // 修改
        target.public = false
        sender.sendMsgWithPrefix("&a成功修改&6${name}&a为私有warp, 只允许信任者传送至此warp")
        WarpManager.save()
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>, complete: MutableList<String>) {
        if (args.size == 2) complete.addAllWarp()
    }

    override fun help() = """&a/ewa private <名字>&f - 设置warp为私有
        |  &f只允许信任者传送至此warp""".trimMargin().color()
}
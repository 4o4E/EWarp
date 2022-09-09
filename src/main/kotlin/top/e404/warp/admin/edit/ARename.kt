package top.e404.warp.admin.edit

import org.bukkit.command.CommandSender
import top.e404.warp.command.AbstractCommand
import top.e404.warp.util.Log.color
import top.e404.warp.util.sendMsgWithPrefix
import top.e404.warp.warp.WarpManager
import top.e404.warp.warp.addAllWarp

object ARename : AbstractCommand(
    "rename",
    false,
    "ewarp.admin",
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 3, sender)) return
        val old = args[1]
        val new = args[2]
        if (old == new) {
            sender.sendMsgWithPrefix("&c新旧名字不可相同")
            return
        }
        val target = WarpManager.warps[old]
        // 不存在的warp
        if (target == null) {
            sender.sendMsgWithPrefix("&c不存在名为&6${old}&c的warp")
            return
        }
        // 重命名
        WarpManager.warps.remove(old)
        target.name = new
        WarpManager.warps[new] = target
        WarpManager.save()
        sender.sendMsgWithPrefix("&a重命名完成, &7${old}&a现在的名字为&6${new}")
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>, complete: MutableList<String>) {
        if (args.size == 2) complete.addAllWarp()
    }

    override fun help() = "&a/ewa rename <旧名字> <新名字>&f - 重命名warp".color()
}
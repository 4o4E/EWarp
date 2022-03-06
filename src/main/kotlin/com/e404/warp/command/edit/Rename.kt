package com.e404.warp.command.edit

import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.util.sendMsgWithPrefix
import com.e404.warp.warp.WarpManager
import com.e404.warp.warp.addOwner
import org.bukkit.command.CommandSender

object Rename : AbstractCommand(
    "rename",
    true,
    "ewarp.use",
    "ewarp.edit"
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
            sender.sendMsgWithPrefix("&c你还未创建名为&6${old}&c的warp")
            return
        }
        // 目标不可修改
        if (!target.canEdit(sender)) {
            sender.sendMsgWithPrefix("&c无权限")
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
        if (args.size == 2) complete.addOwner(sender)
    }

    override fun help() = "&a/ew rename <旧名字> <新名字>&f - 重命名warp".color()
}
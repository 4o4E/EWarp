package com.e404.warp.command.edit

import com.e404.boom.util.sendMsgWithPrefix
import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.warp.WarpManager
import com.e404.warp.warp.addOwner
import org.bukkit.command.CommandSender

object Public : AbstractCommand(
    "public",
    true,
    "ewarp.use",
    "ewarp.edit"
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 2, sender)) return
        val name = args[1]
        val target = WarpManager.warps[name]
        // 不存在的warp
        if (target == null) {
            sender.sendMsgWithPrefix("&c你还未创建名为&6${name}&c的warp")
            return
        }
        // 不可修改的warp
        if (!target.canEdit(sender)) {
            sender.sendMsgWithPrefix("&c你无权修改此warp")
            return
        }
        // 无效修改
        if (target.public) {
            sender.sendMsgWithPrefix("&7此warp已是公开状态")
            return
        }
        // 修改
        target.public = true
        sender.sendMsgWithPrefix("&a成功修改&6${name}&a为公开warp, 允许任何人传送至此warp")
        WarpManager.save()
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>, complete: MutableList<String>) {
        if (args.size == 2) complete.addOwner(sender)
    }

    override fun help() = """&a/ew public <名字>&f - 设置warp为公开
        |  &f允许任何人传送至此warp""".trimMargin().color()
}
package com.e404.warp.admin.edit

import com.e404.boom.util.sendMsgWithPrefix
import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.warp.WarpManager
import com.e404.warp.warp.addAllWarp
import org.bukkit.command.CommandSender

object APublic : AbstractCommand(
    "public",
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
        if (args.size == 2) complete.addAllWarp()
    }

    override fun help() = """&a/ewa public <名字>&f - 设置warp为公开
        |  &f允许任何人传送至此warp""".trimMargin().color()
}
package com.e404.warp.command.trust

import com.e404.boom.util.sendMsgWithPrefix
import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.warp.WarpManager
import com.e404.warp.warp.addTrust
import org.bukkit.command.CommandSender

object Pdel : AbstractCommand("pdel",
    true,
    "ewarp.use",
    "ewarp.trust",
    "ewarp.trust"
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 3, sender)) return
        val name = args[1]
        val warps = WarpManager.getCanEdit(sender)
        // 不存在warp
        if (warps.isEmpty()) {
            sender.sendMsgWithPrefix("&c你还没有创建warp哦")
            return
        }
        // 移除该信任者
        var count = 0
        for (warp in warps.values) if (warp.delTrust(name)) count++
        if (count == 0) {
            sender.sendMsgWithPrefix("&c你的所有warp都不存在信任者&6${name}")
            return
        }
        sender.sendMsgWithPrefix("&a成功在&6${count}个warp&a中移除信任者${name}")
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>, complete: MutableList<String>) {
        if (args.size == 2) complete.addTrust(sender)
    }

    override fun help(): String = """&a/ew pdel <信任者名字>&f - 向你拥有的所有warp移除信任者
        |  &f信任者可用访问你设置为私有的warp""".trimMargin().color()
}
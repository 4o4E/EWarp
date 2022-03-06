package com.e404.warp.command.trust

import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.util.sendMsgWithPrefix
import com.e404.warp.warp.WarpManager
import com.e404.warp.warp.addCanEdit
import com.e404.warp.warp.addOnlineWithoutSelf
import org.bukkit.command.CommandSender

object Padd : AbstractCommand(
    "padd",
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
            sender.sendMsgWithPrefix("&c你还没有创建名为&6${name}&c的warp哦")
            return
        }
        // 添加该信任者
        var count = 0
        for (warp in warps.values) if (warp.addTrust(name)) count++
        if (count == 0) {
            sender.sendMsgWithPrefix("&c你的所有warp都已存在信任者&6${name}")
            return
        }
        sender.sendMsgWithPrefix("&a成功在&6${count}个warp&a中添加信任者${name}")
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>, complete: MutableList<String>) {
        when (args.size) {
            2 -> complete.addCanEdit(sender)
            3 -> complete.addOnlineWithoutSelf(sender)
        }
    }

    override fun help(): String = """&a/ew padd <信任者名字>&f - 向你拥有的所有warp添加信任者
        |  &f信任者可用访问你设置为私有的warp""".trimMargin().color()
}
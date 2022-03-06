package com.e404.warp.admin.trust

import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.util.sendMsgWithPrefix
import com.e404.warp.warp.WarpManager
import com.e404.warp.warp.addAllWarp
import com.e404.warp.warp.addOnlineWithoutSelf
import org.bukkit.command.CommandSender

object ATrust : AbstractCommand(
    "trust",
    false,
    "ewarp.admin",
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 3, sender)) return
        val name = args[1]
        val trust = args[2]
        val target = WarpManager.warps[name]
        // 不存在的warp
        if (target == null) {
            sender.sendMsgWithPrefix("&c不存在名为&6${name}&c的warp")
            return
        }
        // 已有此信任者
        if (target.trusts.any { it.equals(trust, true) }) {
            sender.sendMsgWithPrefix("&6${name}&c的信任者列表中已存在玩家${trust}")
            return
        }
        // 修改
        target.trusts.add(trust)
        sender.sendMsgWithPrefix("""&a成功在&6${name}&a中添加信任者${trust}
            |&a当前信任者有: &7[&6${target.trusts.joinToString("&7, &6")}&7]""".trimMargin())
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>, complete: MutableList<String>) {
        when (args.size) {
            2 -> complete.addAllWarp()
            3 -> complete.addOnlineWithoutSelf(sender)
        }
    }

    override fun help() = """&a/ewa trust <warp名字> <信任者名字>&f - 添加信任者
        |  &f信任者可用访问你设置为私有的warp""".trimMargin().color()
}
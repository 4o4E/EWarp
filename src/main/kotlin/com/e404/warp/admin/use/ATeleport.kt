package com.e404.warp.admin.use

import com.e404.boom.util.sendMsgWithPrefix
import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.warp.WarpManager
import com.e404.warp.warp.addAllWarp
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ATeleport : AbstractCommand(
    "tp",
    true,
    "ewarp.admin",
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 2, sender)) return
        val p = sender as Player
        val name = args[1]
        val warp = WarpManager.warps[name]
        // 不存在/不可见的warp
        if (warp == null) {
            sender.sendMsgWithPrefix("&c没有名为&6${name}&c的warp")
            return
        }
        // 传送
        warp.adminTeleport(p)
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>, complete: MutableList<String>) {
        if (args.size == 2) complete.addAllWarp()
    }

    override fun help() = "&a/ewa tp <名字>&f - 传送到warp".color()
}
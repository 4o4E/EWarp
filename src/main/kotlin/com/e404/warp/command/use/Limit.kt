package com.e404.warp.command.use

import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.warp.WarpManager
import com.e404.warp.warp.every
import com.e404.warp.warp.start
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.math.max

object Limit : AbstractCommand(
    "limit",
    true,
    "ewarp.use",
) {
    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        // 展示
        val p = sender as Player
        val warps = WarpManager.getOwner(p)
        val count = warps.size
        var price = start() + count * every()
        if (p.hasPermission("ewarp.bypass.price")) price = 0.0
        val limit = WarpManager.getWarpLimit(p)
        val max = if (limit == -1) "∞" else max(limit - count, 0).toString()
        p.sendMessage("""&f-=[ &6限制查询 &f]=-
            |&f你创建了&2${count}&f个warp
            |&f包括 &7[&6${warps.keys.joinToString("&7, &6")}&7]
            |&f还可以创建&2${max}&f个warp
            |&f下一个warp的创建价格为&6${price}"""
            .trimMargin()
            .color())
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
    }

    override fun help() = """&a/ew limit&f - 查询创建限制和价格
        |  &f根据创建的数量, 最终的价格会有所不同""".trimMargin().color()
}
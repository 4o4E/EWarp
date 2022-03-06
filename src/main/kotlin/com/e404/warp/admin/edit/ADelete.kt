package com.e404.warp.admin.edit

import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.util.config
import com.e404.warp.util.sendMsgWithPrefix
import com.e404.warp.warp.WarpManager
import com.e404.warp.warp.`return`
import com.e404.warp.warp.addAllWarp
import org.bukkit.command.CommandSender

object ADelete : AbstractCommand(
    "del",
    false,
    "ewarp.admin",
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 2, sender)) return
        val name = args[1]
        val warp = WarpManager.warps[name]
        // 不存在的warp
        if (warp == null) {
            sender.sendMsgWithPrefix("&c不存在名为&6${name}&c的warp")
            return
        }
        // 删除
        WarpManager.warps.remove(name)
        val price: Double
        if (config().getBoolean("price.enable")) {
            price = `return`() / 100 * warp.price
            if (price > 0.0) {
                sender.sendMsgWithPrefix("&6${name}&f已删除, 价值&6${warp.price}&f, ")
                return
            }
        }
        sender.sendMsgWithPrefix("&6${name}&f已删除")
        WarpManager.save()
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>, complete: MutableList<String>) {
        if (args.size == 2) complete.addAllWarp()
    }

    override fun help() = "&a/ewa del <名字>&f - 删除warp".color()
}
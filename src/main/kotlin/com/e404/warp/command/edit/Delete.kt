package com.e404.warp.command.edit

import com.e404.boom.util.config
import com.e404.boom.util.sendMsgWithPrefix
import com.e404.warp.command.AbstractCommand
import com.e404.warp.hook.EconHook
import com.e404.warp.util.Log.color
import com.e404.warp.warp.WarpManager
import com.e404.warp.warp.`return`
import com.e404.warp.warp.addCanEdit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Delete : AbstractCommand(
    "del",
    true,
    "ewarp.use",
    "ewarp.edit"
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数
        if (errorArgsLength(args, 2, sender)) return
        val name = args[1]
        val warp = WarpManager.getOwner(sender as Player)[name]
        // 不存在的warp
        if (warp == null) {
            sender.sendMsgWithPrefix("&c你还未创建名为&6${name}&c的warp")
            return
        }
        // 删除
        WarpManager.warps.remove(name)
        val price: Double
        if (config().getBoolean("price.enable")) {
            price = `return`() / 100 * warp.price
            if (price > 0.0) {
                EconHook.give(sender, price)
                sender.sendMsgWithPrefix("&6${name}&f已删除, 返还金钱&6${price}")
                return
            }
        }
        sender.sendMsgWithPrefix("&6${name}&f已删除")
        WarpManager.save()
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
        if (args.size == 2) complete.addCanEdit(sender)
    }

    override fun help() = "&a/ew del <名字>&f - 删除warp".color()
}
package top.e404.warp.command.edit

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.e404.warp.command.AbstractCommand
import top.e404.warp.hook.EconHook
import top.e404.warp.util.Log.color
import top.e404.warp.util.config
import top.e404.warp.util.sendMsgWithPrefix
import top.e404.warp.warp.WarpManager
import top.e404.warp.warp.addOwner
import top.e404.warp.warp.`return`

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
        if (args.size == 2) complete.addOwner(sender)
    }

    override fun help() = "&a/ew del <名字>&f - 删除warp".color()
}
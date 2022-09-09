package top.e404.warp.command.use

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.e404.warp.command.AbstractCommand
import top.e404.warp.util.Log.color
import top.e404.warp.util.sendMsgWithPrefix
import top.e404.warp.warp.WarpManager
import top.e404.warp.warp.addCanUse

object Teleport : AbstractCommand(
    "tp",
    true,
    "ewarp.use",
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 2, sender)) return
        val p = sender as Player
        val canSee = WarpManager.getCanUse(p)
        val name = args[1]
        val warp = canSee[name]
        // 不存在/不可见的warp
        if (warp == null) {
            sender.sendMsgWithPrefix("&c没有名为&6${name}&c的公开warp, 可用的warp有 &7[&6${canSee.keys.joinToString("&7, &6")}&7]")
            return
        }
        // 传送
        warp.teleport(p)
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>, complete: MutableList<String>) {
        if (args.size == 2) complete.addCanUse(sender)
    }

    override fun help() = "&a/ew tp <名字>&f - 传送到warp".color()
}
package top.e404.warp.admin.manage

import org.bukkit.command.CommandSender
import top.e404.warp.command.AbstractCommand
import top.e404.warp.util.Log.color

object Reload : AbstractCommand("reload", false, "ewarp.admin") {
    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        top.e404.warp.EWarp.load(sender)
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
    }

    override fun help() = "&a/ewa reload&f - 重载插件配置(不包括数据文件)".color()
}
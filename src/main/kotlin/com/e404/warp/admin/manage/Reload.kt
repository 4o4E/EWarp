package com.e404.warp.admin.manage

import com.e404.warp.EWarp
import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import org.bukkit.command.CommandSender

object Reload : AbstractCommand("reload", false, "ewarp.admin") {
    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        EWarp.load(sender)
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
    }

    override fun help() = "&a/ewa reload&f - 重载插件配置(不包括数据文件)".color()
}
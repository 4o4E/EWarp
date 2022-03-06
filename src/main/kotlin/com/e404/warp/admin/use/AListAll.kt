package com.e404.warp.admin.use

import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.util.sendMsgWithPrefix
import com.e404.warp.warp.WarpManager
import org.bukkit.command.CommandSender

/**
 * 列出所有warp
 */
object AListAll : AbstractCommand(
    "listall",
    false,
    "ewarp.admin",
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 1, sender)) return
        sender.sendMsgWithPrefix("&a当前可用warp: &7[&6${WarpManager.warps.keys.joinToString("&7, &6")}&7]")
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
    }

    override fun help() = "&a/ewa listall&f - 查看所有warp".color()
}
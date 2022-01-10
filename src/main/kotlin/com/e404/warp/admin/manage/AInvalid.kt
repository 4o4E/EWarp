package com.e404.warp.admin.manage

import com.e404.boom.util.sendMsgWithPrefix
import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.warp.WarpManager
import org.bukkit.command.CommandSender

/**
 * 列出所有warp
 */
object AInvalid : AbstractCommand(
    "invalid",
    false,
    "ewarp.admin",
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 1, sender)) return
        val invalid = WarpManager.warps
            .filter { it.value.isInvalid() }
            .map { it.key }
        sender.sendMsgWithPrefix("&a无效的warp: &7[&6${invalid.joinToString("&7, &6")}&7]")
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
    }

    override fun help() = "&a/ewa invalid&f - 查看所有无效的warp".color()
}
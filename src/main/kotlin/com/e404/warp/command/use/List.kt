package com.e404.warp.command.use

import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.util.sendMsgWithPrefix
import com.e404.warp.warp.WarpManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object List : AbstractCommand(
    "list",
    true,
    "ewarp.use",
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        // 检查参数长度
        if (errorArgsLength(args, 1, sender)) return
        val list = WarpManager.getOwner(sender as Player).keys.joinToString("&7, &6")
        sender.sendMsgWithPrefix("&f你创建的warp有: &7[&6${list}&7]")
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
    }

    override fun help() = "&a/ew list&f - 查看自己的warp".color()
}
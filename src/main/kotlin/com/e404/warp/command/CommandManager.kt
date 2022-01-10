package com.e404.warp.command

import com.e404.boom.util.isPlayer
import com.e404.boom.util.sendNoperm
import com.e404.boom.util.sendUnknow
import com.e404.warp.command.edit.*
import com.e404.warp.command.trust.Padd
import com.e404.warp.command.trust.Pdel
import com.e404.warp.command.trust.Trust
import com.e404.warp.command.trust.Untrust
import com.e404.warp.command.use.*
import com.e404.warp.command.use.List
import com.e404.warp.util.Log.color
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object CommandManager : TabExecutor {
    private val commands = listOf(
        Create,
        Delete,
        Info,
        ListAll,
        List,
        Padd,
        Pdel,
        Private,
        Public,
        Rename,
        Teleport,
        Trust,
        Untrust,
        Limit
    )

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): Boolean {
        if (args.isEmpty()) {
            sender.sendHelp()
            return true
        }
        val head = args[0].lowercase()
        if (head == "help") {
            sender.sendHelp()
            return true
        }
        for (c in commands) {
            // 匹配指令头
            if (c.name.equals(head, true)) {
                // 无权限
                if (!c.hasPerm(sender)) {
                    sender.sendNoperm()
                    return true
                }
                // 此指令只能由玩家执行 && 执行者不是玩家
                if (c.mustByPlayer && !sender.isPlayer()) return true
                c.onCommand(sender, args)
                return true
            }
        }
        sender.sendUnknow()
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>,
    ): MutableList<String> {
        val list = ArrayList<String>()
        val size = args.size // 最小只会为1
        val canUse = commands.filter { it.hasPerm(sender) }
        // 接管长度为1的指令的tab补全
        if (size == 1) return canUse.map { it.name }.toMutableList()
        // 其他长度
        val head = args[0]
        // 匹配指令头
        for (c in canUse) if (c.matchHead(head)) {
            // 此指令只能由玩家执行 && 执行者不是玩家
            if (c.mustByPlayer && sender !is Player) continue
            // 传递给指令
            c.onTabComplete(sender, args, list)
        }
        return list
    }

    private fun CommandSender.sendHelp() {
        val help = commands
            .filter { it.hasPerm(this) && (!it.mustByPlayer || this is Player) }
            .joinToString("\n") { it.help() }
        sendMessage("&7-=[ &6EWarp&b by 404E &7]=-\n${help}".color())
    }
}
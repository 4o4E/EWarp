package top.e404.warp.admin

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import top.e404.warp.admin.edit.*
import top.e404.warp.admin.manage.AInvalid
import top.e404.warp.admin.manage.Reload
import top.e404.warp.admin.trust.ATrust
import top.e404.warp.admin.trust.AUntrust
import top.e404.warp.admin.use.*
import top.e404.warp.util.Log.color
import top.e404.warp.util.isPlayer
import top.e404.warp.util.sendUnknow

object AdminCommandManager : TabExecutor {
    private val commands = listOf(
        Reload,
        AInvalid,
        ACreate,
        ADelete,
        APrivate,
        APublic,
        ARename,
        ATrust,
        AUntrust,
        AInfo,
        ALimit,
        AList,
        AListAll,
        ATeleport,
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
        // 匹配指令头
        for (c in commands) if (c.name.equals(head, true)) {
            // 无权限
            if (c.mustByPlayer && !sender.isPlayer()) return true
            c.onCommand(sender, args)
            return true
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
        val head = args[0]
        val canUse = commands.filter { it.hasPerm(sender) }
        // 接管长度为1的指令的tab补全
        if (size == 1) return canUse.map { it.name }.toMutableList()
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
            .filter { !it.mustByPlayer || this is Player }
            .joinToString("\n") { it.help() }
        sendMessage("&7-=[ &6EWarp&b by 404E &7]=-\n${help}".color())
    }
}
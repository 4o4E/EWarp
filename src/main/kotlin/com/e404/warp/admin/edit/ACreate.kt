package com.e404.warp.admin.edit

import com.e404.boom.util.config
import com.e404.boom.util.isPlayer
import com.e404.boom.util.sendMsgWithPrefix
import com.e404.warp.command.AbstractCommand
import com.e404.warp.util.Log.color
import com.e404.warp.warp.Warp
import com.e404.warp.warp.WarpManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object ACreate : AbstractCommand(
    "create",
    true,
    "ewarp.admin"
) {
    private fun String.asDouble() = try {
        toDouble()
    } catch (e: Throwable) {
        null
    }

    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        when (args.size) {
            2 -> {
                if (!sender.isPlayer()) return
                val p = sender as Player
                val name = args[1]
                if (!name.matches(Regex(config().getString("name_regex") ?: "[a-zA-Z0-9-_]{1,15}"))) {
                    p.sendMsgWithPrefix("&c此名字不合规范")
                    return
                }
                // 检查重复
                val warp = WarpManager.warps[name]
                if (warp != null) {
                    p.sendMsgWithPrefix(if (!warp.isOwner(p)) "&c此名字的warp已存在"
                    else "&c你已创建过此warp, 若需要覆盖请先使用&a/ew del ${name}&c删除")
                    return
                }
                // 创建
                val create = WarpManager.sdf.format(Date())
                val l = p.location
                WarpManager.warps[name] = Warp(name, p.name, create, true, 0.0, p.world.name,
                    l.x.format(), l.y.format(), l.z.format(), l.pitch.format(), l.yaw.format())
                p.sendMsgWithPrefix("&6${name}&a创建成功")
                WarpManager.save()
            }
            6 -> {
                val name = args[1]
                if (!name.matches(Regex(config().getString("name_regex") ?: "[a-zA-Z0-9-_]{1,15}"))) {
                    sender.sendMsgWithPrefix("&c此名字不合规范")
                    return
                }
                // 检查重复
                val warp = WarpManager.warps[name]
                if (warp != null) {
                    sender.sendMsgWithPrefix("&c此名字的warp已存在")
                    return
                }
                // 检查参数
                val world = args[2]
                val sx = args[3].asDouble()
                val sy = args[4].asDouble()
                val sz = args[5].asDouble()
                when {
                    Bukkit.getWorld(world) == null -> sender.sendMsgWithPrefix("&c不存在名为&6${world}&c的世界")
                    sx == null -> sender.sendMsgWithPrefix("&cX的数值错误")
                    sy == null -> sender.sendMsgWithPrefix("&cY的数值错误")
                    sz == null -> sender.sendMsgWithPrefix("&cZ的数值错误")
                    else -> {
                        WarpManager.warps[name] = Warp(name, sender.name, WarpManager.sdf.format(Date()),
                            true, 0.0, world, sx, sy, sz, 0f, 0f)
                        sender.sendMsgWithPrefix("&6${name}&a创建成功")
                        WarpManager.save()
                    }
                }
            }
            else -> sender.sendMessage(help())
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
        if (args.size == 3) complete.addAll(Bukkit.getWorlds().map { it.name })
    }

    override fun help() = """&a/ewa create <名字>&f - 在当前位置创建warp(仅玩家可用)
        |&a/ewa create <名字> <世界> <x> <y> <z>&f - 创建warp"""
        .trimMargin()
        .color()

    private fun Double.format(): Double {
        return String.format("%.2f", this).toDouble()
    }

    private fun Float.format(): Float {
        return String.format("%.2f", this).toFloat()
    }
}
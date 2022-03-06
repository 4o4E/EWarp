package com.e404.warp.command.edit

import com.e404.warp.command.AbstractCommand
import com.e404.warp.hook.EconHook
import com.e404.warp.hook.HookManager
import com.e404.warp.util.Log.color
import com.e404.warp.util.config
import com.e404.warp.util.sendMsgWithPrefix
import com.e404.warp.warp.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object Create : AbstractCommand(
    "create",
    true,
    "ewarp.use",
    "ewarp.edit"
) {
    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        val p = sender as Player
        // 检查参数长度
        if (errorArgsLength(args, 2, sender)) return
        // 检查是否是禁用的世界
        if (config().getStringList("disable_create").contains(p.world.name)) {
            p.sendMsgWithPrefix("&c此世界禁止创建warp")
            return
        }
        // hook检查
        if (!HookManager.canCreate(p)) return
        var count = 0
        // 检查上限
        if (!isUnlimited(p)
            && WarpManager.getOwner(p).size.also { count = it } >= limit()
        ) {
            p.sendMsgWithPrefix("&c你创建的warp数量已达上限")
            return
        }
        // 名字检查
        val name = args[1]
        if (!name.matches(Regex(config().getString("name_regex") ?: "[a-zA-Z0-9-_]{1,15}"))) {
            p.sendMsgWithPrefix("&c名字`$name`不合规范")
            return
        }
        // 检查重复
        val warp = WarpManager.warps[name]
        if (warp != null) {
            p.sendMsgWithPrefix(if (!warp.isOwner(sender)) "&c此名字的warp已存在"
            else "&c你已创建过此warp, 若需要覆盖请先使用&a/ew del ${name}&c删除")
            return
        }
        // 计算高度限制
        val height = p.location.y
        val max = config().getDouble("max_height")
        val min = config().getDouble("min_height")
        if (height !in min..max) {
            p.sendMsgWithPrefix("&c你的高度&4${height}&c超出允许创建的高度&a$min-$max")
            return
        }
        // 计算消耗
        var price = 0.0
        if (config().getBoolean("price.enable")) {
            if (!p.hasPermission("ewarp.bypass.price")) {
                price = start() + count * every()
            }
            // 若余额不足则提示
            if (!EconHook.take(p, price)) {
                p.sendMsgWithPrefix("&c余额不足, 创建需要${price}")
                return
            }
        }
        // 创建
        val create = WarpManager.sdf.format(Date())
        val l = p.location
        WarpManager.warps[name] = Warp(name, p.name, create, true, price, p.world.name,
            l.x.format(), l.y.format(), l.z.format(), l.pitch.format(), l.yaw.format())
        p.sendMsgWithPrefix("&6${name}&a创建成功${if (price == 0.0) "" else ", 消耗金钱${price}"}")
        WarpManager.save()
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
    }

    override fun help() = "&a/ew create <名字>&f - 创建warp".color()

    private fun Double.format(): Double {
        return String.format("%.2f", this).toDouble()
    }

    private fun Float.format(): Float {
        return String.format("%.2f", this).toFloat()
    }
}
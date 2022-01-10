package com.e404.warp.warp

import com.e404.boom.util.config
import com.e404.boom.util.sendMsgWithPrefix
import com.e404.warp.EWarp
import com.e404.warp.hook.MultiverseHook
import com.e404.warp.util.Log.color
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.math.min

/**
 * 代表一个warp
 *
 * @property name 此warp的名字
 * @property owner 创建者
 * @property date 创建日期
 * @property public 公开
 * @property price 价格
 * @property world 世界名字
 * @property x x坐标
 * @property y y坐标
 * @property z z坐标
 * @property pitch 俯仰角
 * @property yaw 方向
 * @property trusts 信任列表
 */
class Warp(
    var name: String,
    var owner: String,
    var date: String,
    var public: Boolean,
    var price: Double,
    var world: String,
    var x: Double,
    var y: Double,
    var z: Double,
    var pitch: Float,
    var yaw: Float,
    var trusts: ArrayList<String> = ArrayList(),
) {
    companion object {
        private val cooldown = HashMap<Player, Long>()
    }

    /**
     * 检查是否对此sender隐藏(若隐藏则不可见&不可用)
     *
     * @param sender CommandSender
     * @return 若不对其隐藏则返回true
     */
    fun canSee(sender: CommandSender): Boolean {
        //if (isAdmin(sender)) return true
        return if (public) true
        else isOwner(sender) || isTrust(sender)
    }

    /**
     * 检查此sender是否可修改此warp
     *
     * @param sender CommandSender
     * @return 若可修改则返回true
     */
    fun canEdit(sender: CommandSender): Boolean {
        //if (isAdmin(sender)) return true
        return isOwner(sender)
    }

    /**
     * 返回此sender是否在信任者列表中
     *
     * @param sender CommandSender
     * @return 若在信任者列表中则返回true
     */
    fun isTrust(sender: CommandSender): Boolean {
        return trusts.any { it.equals(sender.name, true) }
    }

    /**
     * 返回此sender是否为创建者
     *
     * @param sender CommandSender
     * @return 若是创建者则返回true
     */
    fun isOwner(sender: CommandSender): Boolean {
        return owner.equals(sender.name, true)
    }

    /**
     * 返回此名字是否为创建者
     *
     * @param playerName 名字
     * @return 若为创建者则返回true
     */
    fun isOwner(playerName: String): Boolean {
        return owner.equals(playerName, true)
    }

    private fun location(): Location? {
        val world = Bukkit.getWorld(this.world) ?: return null
        return Location(world, x, y, z, yaw, pitch)
    }

    /**
     * 将玩家传送至此warp
     *
     * @param p 玩家
     * @return 若成功则返回true, 若此世界已删除则返回false
     */
    fun teleport(p: Player) {
        // 检查是否是禁用的世界
        if (config().getStringList("disable_use").contains(world)) {
            p.sendMsgWithPrefix("&c此世界禁止使用warp")
            return
        }
        // 检查cd
        if (!p.hasPermission("ewarp.bypass.cooldown")) {
            val cd = config().getLong("teleport.cooldown")
            if (cd >= 0) {
                val last = System.currentTimeMillis() - cooldown.getOrDefault(p, 0) - cd
                if (last <= 0) {
                    p.sendMsgWithPrefix("&c还在cd中, 剩余&f${-last / 1000}&c秒")
                    return
                }
            }
        }
        // 检查预热
        val location = location()
        // 是否存在
        if (location == null) {
            p.sendMsgWithPrefix("&c无效warp, 使用&a/ew del ${name}&c以删除")
            return
        }
        if (!p.hasPermission("ewarp.bypass.warmup")) {
            val warmup = config().getLong("teleport.warmup")
            if (warmup >= 0) {
                val tick = warmup / 50
                Bukkit.getScheduler().runTaskLater(EWarp.instance, fun() {
                    cooldown[p] = System.currentTimeMillis()
                    p.teleport(location)
                    p.playSound(p.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
                    p.sendMsgWithPrefix("&f已到达&a${name}")
                }, tick)
                val fade = min(tick.toInt() / 4, 60)
                p.sendTitle("&6请等待${warmup / 1000}秒".color(), "", fade, tick.toInt() - 2 * fade, fade)
                return
            }
        }
        p.teleport(location)
        cooldown[p] = System.currentTimeMillis()
        p.sendMsgWithPrefix("&f已前往&a${name}")
    }

    /**
     * 将玩家传送至此warp
     *
     * @param p 玩家
     * @return 若成功则返回true, 若此世界已删除则返回false
     */
    fun adminTeleport(p: Player) {
        val location = location()
        // 是否存在
        if (location == null) {
            p.sendMsgWithPrefix("&c无效warp, 世界${MultiverseHook.parse(world)}不存在, 使用&a/ewa del ${name}&c以删除")
            return
        }
        p.teleport(location)
        p.sendMsgWithPrefix("&f已前往&a${name}")
    }

    fun fullInfo(sender: CommandSender) {
        sender.sendMessage("""&6=-=-=-=-=-=-=-=-=-=-=-=
            |&f名字: &6$name &f属于: &2$owner &f状态: ${if (public) "&a公开" else "&c私人"}
            |&f创建于: &2$date
            |&f世界: &2${MultiverseHook.parse(world)}
            |&fx: &2${x}&f, y: &2${y}&f, z: &2${z}
            |&f价格: &6${price}
            |&f信任者: &2[${trusts.joinToString(", ")}]
            |&6=-=-=-=-=-=-=-=-=-=-=-="""
            .trimMargin()
            .color())
    }

    /**
     * 向玩家展示warp信息, 当owner查看时展示trust
     *
     * @param sender 展示对象
     */
    fun info(sender: CommandSender) {
        if (isOwner(sender)) fullInfo(sender)
        else sender.sendMessage("""&6=-=-=-=-=-=-=-=-=-=-=-=
            |&f名字: &6$name &f属于: &2$owner &f状态: ${if (public) "&a公开" else "&c私人"}
            |&f创建于: &2$date
            |&f世界: &2${MultiverseHook.parse(world)}
            |&fx: &2${x}&f, y: &2${y}&f, z: &2${z}
            |&6=-=-=-=-=-=-=-=-=-=-=-="""
            .trimMargin()
            .color())
    }

    /**
     * 检查此warp是否无效
     *
     * @return 若无效则返回true
     */
    fun isInvalid(): Boolean {
        return location() == null
    }

    /**
     * 添加信任者, 若已存在则不添加
     *
     * @param name 信任者名字
     * @return 若成功添加则返回true
     */
    fun addTrust(name: String): Boolean {
        if (trusts.any { it.equals(name, true) }) return false
        trusts.add(name)
        return true
    }

    /**
     * 移除信任者
     *
     * @param name 信任者名字
     * @return 若成功移除则返回true
     */
    fun delTrust(name: String): Boolean {
        if (!trusts.any { it.equals(name, true) }) return false
        trusts.removeIf { it.equals(name, true) }
        return true
    }
}
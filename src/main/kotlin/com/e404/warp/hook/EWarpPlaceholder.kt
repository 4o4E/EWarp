package com.e404.warp.hook

import com.e404.boom.util.instance
import com.e404.warp.util.Log
import com.e404.warp.warp.WarpManager
import com.e404.warp.warp.count
import com.e404.warp.warp.every
import com.e404.warp.warp.start
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import java.text.SimpleDateFormat

/**
 * # 对应玩家的
 * ```
 * %ewarp_max% - 创建上限(若不受限制则返回-1)
 * %ewarp_max_parse% - 创建上限(若不受限制则返回'∞')
 * %ewarp_exist% - 已创建数量
 * %ewarp_price% - 下一个创建的价格
 * ```
 * # 对应名字的
 * ```
 * %ewarp_{name}_owner% - 对应warp的创建者
 * %ewarp_{name}_price% - 对应warp的创建价格
 * %ewarp_{name}_x% - 对应warp的x坐标
 * %ewarp_{name}_y% - 对应warp的y坐标
 * %ewarp_{name}_z% - 对应warp的z坐标
 * %ewarp_{name}_pitch% - 对应warp的pitch(俯仰角)
 * %ewarp_{name}_yaw% - 对应warp的yaw(方向)
 * %ewarp_{name}_public% - 对应warp的状态
 * %ewarp_{name}_public_{1}_{2}% - 对应warp的状态, 若公开则返回{1}否则{2}
 * %ewarp_{name}_world% - 对应warp的世界名字
 * %ewarp_{name}_world_parse% - 对应warp的multiverse世界名字
 * %ewarp_{name}_date% - 对应warp的创建日期
 * %ewarp_{name}_date_{日期格式}% - 对应warp的创建日期并格式化
 * %ewarp_{name}_trusts% - 对应warp的信任者列表(分隔符',')
 * %ewarp_{name}_trusts_{分隔符}% - 对应warp的信任者列表(自定义分隔符)
 * ```
 */
object EWarpPlaceholder : PlaceholderExpansion() {
    override fun getIdentifier() = "ewarp"
    override fun getAuthor() = "404E"
    override fun getVersion() = instance().description.version

    override fun onPlaceholderRequest(p: Player?, s: String): String? {
        return when (s.lowercase()) {
            "max" -> p?.let { WarpManager.getWarpLimit(it).toString() }
            "max_parse" -> p?.let {
                WarpManager.getWarpLimit(it).let { limit ->
                    if (limit == -1) "∞" else limit.toString()
                }
            }
            "exist" -> p?.let { count(it).toString() }
            "price" -> p?.let {
                if (it.hasPermission("ewarp.limit.*")) "0"
                else (start() + count(it) * every()).toString()
            }
            else -> {
                val split = s.split("_")
                if (split.size == 1) return null
                val name = split[0]
                val warp = WarpManager.warps[name] ?: return null
                return when (split[1].lowercase()) {
                    "owner" -> warp.owner
                    "price" -> warp.price.toString()
                    "x" -> warp.x.toString()
                    "y" -> warp.y.toString()
                    "z" -> warp.z.toString()
                    "pitch" -> warp.pitch.toString()
                    "yaw" -> warp.yaw.toString()
                    "public" -> return when {
                        split.size == 2 -> warp.public.toString()
                        split.size != 4 -> null
                        warp.public -> split[2]
                        else -> split[3]
                    }
                    "world" -> if (split.size == 2) warp.world
                    else MultiverseHook.parse(warp.world)
                    "date" -> if (split.size == 2) warp.date
                    else try {
                        val date = WarpManager.sdf.parse(warp.date)
                        val sdf = SimpleDateFormat(split.subList(2, split.size).joinToString("_"))
                        sdf.format(date)
                    } catch (e: Throwable) {
                        Log.warn("解析papi时出现异常, papi: ${s}", e)
                        null
                    }
                    "trusts" -> if (split.size == 2) warp.trusts.joinToString(",")
                    else warp.trusts.joinToString(split[2])
                    else -> null
                }
            }
        }
    }
}
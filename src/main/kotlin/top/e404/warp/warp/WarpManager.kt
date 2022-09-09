package top.e404.warp.warp

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.e404.warp.util.doWarnable
import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

object WarpManager {
    private val file = File(top.e404.warp.EWarp.instance.dataFolder, "warps.json")
    private val gb = GsonBuilder().setPrettyPrinting().create()
    val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
    var warps = HashMap<String, Warp>()
    private val permRegex = Regex("(?i)ewarp\\.limit\\.\\d+")

    /**
     * 获取玩家的warp创建上限, 检查会在权限节点和配置文件最大值中选择最大的
     *
     * @return 上限数量, 若没有上限则返回-1
     */
    fun getWarpLimit(p: Player): Int {
        if (p.hasPermission("ewarp.limit.*")) return -1
        val configLimit = limit()
        if (configLimit < 0) return -1
        val permLimit = p.effectivePermissions
            .filter { it.permission.matches(permRegex) && it.value }
            .map { it.permission.level() }
            .sortedByDescending { it }
        return if (permLimit.isEmpty()) configLimit
        else max(configLimit, permLimit[0])
    }

    private fun String.level() = substring(lastIndexOf('.') + 1, length).toInt()

    /**
     * 获取此玩家可见的warp(owner/trust)
     *
     * @param sender 玩家
     * @return 对其可见的warp列表
     */
    fun getCanUse(sender: CommandSender): Map<String, Warp> {
        return warps.entries
            .filter { it.value.canSee(sender) }
            .associate { it.toPair() }
    }

    /**
     * 获取此玩家可以修改的warp(owner)
     *
     * @param sender
     * @return
     */
    fun getCanEdit(sender: CommandSender): Map<String, Warp> {
        return if (sender.hasPermission("ewarp.edit.other")) HashMap(warps)
        else warps.entries
            .filter { it.value.canEdit(sender) }
            .associate { it.toPair() }
    }

    /**
     * 获取玩家创建的所有warp
     *
     * @param p 玩家
     * @return 此玩家创建的所有warp
     */
    fun getOwner(p: Player): Map<String, Warp> {
        return warps.entries.filter { it.value.isOwner(p) }.associate { it.toPair() }
    }

    /**
     * 获取玩家创建的所有warp
     *
     * @param playerName 玩家名字
     * @return 此玩家创建的所有warp
     */
    fun getOwner(playerName: String): Map<String, Warp> {
        return warps.entries.filter { it.value.isOwner(playerName) }.associate { it.toPair() }
    }

    /**
     * 获取玩家所有warp中的信任者
     *
     * @param p 玩家
     * @return 所有信任者
     */
    fun getTrust(p: Player): List<String> {
        return getOwner(p).entries
            .flatMap { it.value.trusts }
            .groupBy { it }
            .keys
            .toList()
    }

    fun load() {
        doWarnable("读取warp数据", null) {
            if (file.isDirectory) file.delete()
            if (!file.exists()) top.e404.warp.EWarp.instance.saveResource("warps.json", false)
            warps = gb.fromJson(file.readText(), object : TypeToken<HashMap<String, Warp>>() {}.type)
        }
    }

    private var saving = AtomicBoolean(false)

    fun save() {
        Bukkit.getScheduler().runTaskAsynchronously(top.e404.warp.EWarp.instance, fun() {
            if (saving.get()) return
            saving.set(true)
            doWarnable("保存warp数据", null) {
                file.writeText(gb.toJson(warps))
            }
            saving.set(false)
        })
    }
}


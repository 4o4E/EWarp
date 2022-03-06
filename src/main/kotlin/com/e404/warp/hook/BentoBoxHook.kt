package com.e404.warp.hook

import com.e404.warp.util.sendMsgWithPrefix
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import world.bentobox.bentobox.BentoBox
import kotlin.math.abs

object BentoBoxHook : AbstractHook("BentoBox"), CheckCreate {
    private var box: BentoBox? = null
    private fun api() = plugin()?.let { it as BentoBox }

    override fun hook(sender: CommandSender?) {
        if (!isHookEnable()) {
            noticeUnhook(sender, "在配置文件中禁用了此hook")
            return
        }
        box = api()
        if (box != null) noticeHook(sender)
        else noticeUnhook(sender, "未检测到${pluginName}")
    }

    /**
     * 检查该玩家是否在自己的岛屿上
     *
     * @param p 玩家
     * @return 若在自己岛上则返回true, 若检查未启用也返回true
     */
    override fun canCreate(p: Player): Boolean {
        // 未启用hook
        if (!isHookEnable()) return true
        // 不存在hook对象
        val api = box ?: return true
        val island = api.islandsManager.getIsland(p.world, p.uniqueId)
        // 不存在岛屿的世界不可用
        val l = p.location
        if (island == null || !island.inIslandSpace(l)) {
            p.sendMsgWithPrefix("仅可在岛屿上创建warp")
            return false
        }
        // 该玩家在岛屿上的等级
        val rank = island.members[p.uniqueId] ?: 0
        // rank检查
        val can = rank > (hookConfig()?.getInt("rank") ?: 400)
        if (!can) {
            p.sendMsgWithPrefix("&c你的等级不足以创建warp")
            return false
        }
        // 保护范围
        if (abs(island.center.x - l.x) > island.protectionRange
            || abs(island.center.z - l.z) > island.protectionRange
        ) {
            p.sendMsgWithPrefix("&c不能在岛屿范围外创建warp")
            return false
        }
        return true
    }
}
package com.e404.warp.hook

import com.e404.warp.util.instance
import net.milkbowl.vault.economy.Economy
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object EconHook : AbstractHook("Vault") {
    private var economy: Economy? = null
    private var enable = false

    override fun hook(sender: CommandSender?) {
        if (!isHookEnable()) {
            noticeUnhook(sender, "在配置文件中禁用了此hook")
            return
        }
        // vault不存在
        if (plugin() == null) {
            enable = false
            noticeUnhook(sender, "未检测到Vault")
            return
        }
        // 经济插件不存在
        val rsp = instance().server.servicesManager.getRegistration(Economy::class.java)
        if (rsp == null) {
            enable = false
            noticeUnhook(sender, "未检测到经济插件")
            return
        }
        economy = rsp.provider
        enable = true
        noticeHook(sender)
    }

    /**
     * 从玩家的余额中取走指定的余额
     *
     * @param p 玩家
     * @param amount 取的数值
     * @return 是否成功, 若余额不足则返回false
     */
    fun take(p: Player, amount: Double): Boolean {
        val econ = economy ?: return true
        if (econ.getBalance(p) < amount) return false
        econ.withdrawPlayer(p, amount)
        return true
    }

    /**
     * 给玩家金钱, 若经济插件不可用则return
     *
     * @param p 玩家
     * @param amount 数量
     */
    fun give(p: Player, amount: Double) {
        val econ = economy ?: return
        econ.depositPlayer(p, amount)
    }
}
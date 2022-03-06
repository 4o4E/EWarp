package com.e404.warp.hook

import com.e404.warp.util.config
import com.e404.warp.util.sendAndInfo
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

abstract class AbstractHook(val pluginName: String) {
    protected fun plugin() = Bukkit.getPluginManager().getPlugin(pluginName)
    protected fun hookConfig() = config().getConfigurationSection("hook.${pluginName}")
    protected fun isHookEnable() = hookConfig()?.getBoolean("enable") == true
    abstract fun hook(sender: CommandSender?)

    fun noticeHook(sender: CommandSender?) {
        sender.sendAndInfo("&a已加载与${pluginName}的挂钩")
    }

    fun noticeUnhook(sender: CommandSender?, reason: String) {
        sender.sendAndInfo("&c未加载与${pluginName}的挂钩, 原因: $reason")
    }
}
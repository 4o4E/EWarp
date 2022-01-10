package com.e404.warp.hook

import com.onarandombox.MultiverseCore.MultiverseCore
import com.onarandombox.MultiverseCore.api.MVWorldManager
import org.bukkit.command.CommandSender

object MultiverseHook : AbstractHook("Multiverse-Core") {
    private var mvm: MVWorldManager? = null
    private fun mvm() = plugin()?.let { it as MultiverseCore }?.mvWorldManager
    override fun hook(sender: CommandSender?) {
        if (!isHookEnable()) {
            noticeUnhook(sender, "在配置文件中禁用了此hook")
            return
        }
        mvm = mvm()
        if (mvm != null) {
            noticeHook(sender)
            return
        } else noticeUnhook(sender, "未检测到${pluginName}")
    }

    fun parse(name: String): String {
        if (!isHookEnable()) return name
        return mvm?.getMVWorld(name)?.alias ?: name
    }
}
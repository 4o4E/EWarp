package top.e404.warp.hook

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import top.e404.warp.placeholder.EWarpPlaceholder

object PlaceholderAPIHook : AbstractHook("PlaceholderAPI") {
    var papi = false
    override fun hook(sender: CommandSender?) {
        if (!isHookEnable()) {
            noticeUnhook(sender, "在配置文件中禁用了此hook")
            return
        }
        papi = plugin() != null
        if (papi) {
            EWarpPlaceholder.register()
            noticeHook(sender)
            return
        }
        noticeUnhook(sender, "未找到$pluginName")
    }

    fun String.papi(p: OfflinePlayer): String {
        if (!papi) return this
        return PlaceholderAPI.setPlaceholders(p, this)
    }
}


package top.e404.warp

import org.bstats.bukkit.Metrics
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import top.e404.warp.admin.AdminCommandManager
import top.e404.warp.command.CommandManager
import top.e404.warp.hook.HookManager
import top.e404.warp.hook.PlaceholderAPIHook
import top.e404.warp.placeholder.EWarpPlaceholder
import top.e404.warp.util.Log
import top.e404.warp.util.doWarnable
import top.e404.warp.util.logo
import top.e404.warp.util.sendAndInfo
import top.e404.warp.warp.WarpManager


class EWarp : JavaPlugin() {
    companion object {
        const val prefix = "&7[&6EWarp&7]"
        lateinit var instance: EWarp

        fun load(sender: CommandSender?) {
            doWarnable("保存默认配置文件", sender, instance::saveDefaultConfig)
            doWarnable("读取默认配置文件", sender, instance::reloadConfig)
            sender.sendAndInfo("&a插件配置加载完成")
            HookManager.hook(sender)
            sender.sendAndInfo("&a插件重载完成")
        }
    }

    override fun onEnable() {
        instance = this
        Metrics(this, 13913)
        load(null)
        doWarnable("读取warp数据", null, WarpManager::load)
        instance.getCommand("ewarp")?.let {
            it.setExecutor(CommandManager)
            it.tabCompleter = CommandManager
        }
        instance.getCommand("ewarpadmin")?.let {
            it.setExecutor(AdminCommandManager)
            it.tabCompleter = AdminCommandManager
        }
        for (s in logo().split("\n")) Log.info(s)
        Log.info("&a已成功加载, 作者404E")
    }

    override fun onDisable() {
        if (PlaceholderAPIHook.papi) EWarpPlaceholder.unregister()
        Log.info("&2已成功卸载, 作者404E, 感谢使用")
    }
}
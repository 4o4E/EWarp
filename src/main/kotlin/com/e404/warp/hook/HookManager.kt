package com.e404.warp.hook

import com.e404.boom.util.doWarnable
import com.e404.boom.util.sendAndInfo
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object HookManager {
    private val hooks = listOf(
        BentoBoxHook,
        EconHook,
        MultiverseHook,
        PlaceholderAPIHook
    )

    fun hook(sender: CommandSender?) {
        doWarnable("加载hook", sender) {
            for (hook in hooks) hook.hook(sender)
            sender.sendAndInfo("&ahook加载完成")
        }
    }

    fun canCreate(p: Player): Boolean {
        return !hooks.filter { it is CheckCreate }
            .map { it as CheckCreate }
            .any { !it.canCreate(p) }
    }
}
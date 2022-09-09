package top.e404.warp.warp

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.e404.warp.util.config

fun limit(): Int = config().getInt("limit")
fun start() = config().getDouble("price.start")
fun every() = config().getDouble("price.every")
fun `return`() = config().getDouble("price.return")
fun isUnlimited(p: Player) = p.hasPermission("ewarp.limit.*") || limit() == -1
fun MutableList<String>.addCanUse(sender: CommandSender) {
    addAll(WarpManager.getCanUse(sender).keys)
}

fun MutableList<String>.addCanEdit(sender: CommandSender) {
    addAll(WarpManager.getCanEdit(sender).keys)
}

fun MutableList<String>.addTrust(sender: CommandSender) {
    addAll(WarpManager.getTrust(sender as Player))
}

fun MutableList<String>.addOnline() {
    addAll(Bukkit.getOnlinePlayers().map { it.name })
}

fun MutableList<String>.addOnlineWithoutSelf(sender: CommandSender) {
    addAll(Bukkit.getOnlinePlayers().filter { it != sender }.map { it.name })
}

fun MutableList<String>.addOwner(sender: CommandSender) {
    addAll(WarpManager.getOwner(sender as Player).keys)
}

fun MutableList<String>.addAllWarp() {
    addAll(WarpManager.warps.keys)
}

fun count(p: Player): Int {
    return WarpManager.getOwner(p).size
}
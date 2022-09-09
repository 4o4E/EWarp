package top.e404.warp.util

import java.util.logging.Level
import java.util.logging.Logger

object Log {
    @JvmStatic
    private val log = Logger.getLogger("EWarp")

    @JvmStatic
    fun String.color() = this.replace("&", "§")

    @JvmStatic
    fun info(s: String) = log.info(s.color())

    @JvmStatic
    fun warn(s: String, throwable: Throwable? = null) {
        if (throwable != null) log.log(Level.WARNING, "&c$s".color(), throwable)
        else log.log(Level.WARNING, "&c$s".color())
        noticeOp(s)
    }

    @JvmStatic
    fun noticeOp(s: String) {
        s.sendToOperatorWithPrefix()
    }
}
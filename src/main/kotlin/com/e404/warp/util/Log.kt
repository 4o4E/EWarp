package com.e404.warp.util

import com.e404.boom.util.sendToOperatorWithPrefix
import org.slf4j.LoggerFactory

object Log {
    @JvmStatic
    private val log = LoggerFactory.getLogger("EWarp")

    @JvmStatic
    fun String.color() = this.replace("&", "§")

    @JvmStatic
    fun info(s: String) = log.info(s.color())

    @JvmStatic
    fun warn(s: String, throwable: Throwable? = null) {
        if (throwable != null) log.warn("&c$s".color(), throwable)
        else log.warn("&c$s".color())
        noticeOp(s)
    }

    @JvmStatic
    fun noticeOp(s: String) {
        s.sendToOperatorWithPrefix()
    }
}
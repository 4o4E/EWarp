package top.e404.warp.hook

import org.bukkit.entity.Player

interface CheckCreate {
    /**
     * 检查该玩家是否能够在岛屿上创建warp
     *
     * @param p 玩家
     * @return 若该玩家可以在其当前位置创建warp则返回true
     */
    fun canCreate(p: Player): Boolean
}
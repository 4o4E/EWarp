# EWarp

基于BukkitAPI的传送点插件, 支持设置

1. 玩家创建warp上限
2. 高度限制
3. warp允许的名字（正则）
4. 传送预热及冷却
5. 设置warp的价格（支持价格递增）
6. 设置移除时的返还比例
7. 禁止使用的世界
8. 禁止创建的世界

## 挂钩插件

1. Vault：禁用后将不消耗货币
2. BentBox：限制玩家只能在岛屿上创建warp，支持设置创建warp所需要的身份（岛主/岛员/协作者等）
3. Multiverse-Core：启用后世界名字会被替换成多世界插件设置中的世界名字
4. PlaceholderAPI：启用后支持占位符

## 插件指令

1. /ew create <名字> - 创建warp
2. /ew del <名字> - 删除warp
3. /ew info <名字> - 查看warp详细信息
4. /ew listall - 查看所有可见的warp
5. /ew list - 查看自己的warp
6. /ew padd <信任者名字> - 向你拥有的所有warp添加信任者, 信任者可用访问你设置为私有的warp
7. /ew pdel <信任者名字> - 向你拥有的所有warp移除信任者, 信任者可用访问你设置为私有的warp
8. /ew private <名字> - 设置warp为私有 只允许信任者传送至此warp
9. /ew public <名字> - 设置warp为公开 允许任何人传送至此warp
10. /ew rename <旧名字> <新名字> - 重命名warp
11. /ew tp <名字> - 传送到warp
12. /ew trust <warp名字> <信任者名字> - 添加信任者, 信任者可用访问你设置为私有的warp
13. /ew untrust <warp名字> <信任者名字> - 移除信任者, 信任者可用访问你设置为私有的warp /ew limit - 查询创建限制和价格 ,根据创建的数量, 最终的价格会有所不同

## 管理指令

1. /ew reload - 重载插件配置(不包括数据文件)
2. /ewa invalid - 查看所有无效的warp
3. /ewa create <名字> - 在当前位置创建warp(仅玩家可用)
4. /ewa create <名字> <世界> <x> <y> <z> - 创建warp
5. /ewa del <名字> - 删除warp
6. /ewa private <名字> - 设置warp为私有, 只允许信任者传送至此warp
7. /ewa public <名字> - 设置warp为公开, 允许任何人传送至此warp
8. /ewa rename <旧名字> <新名字> - 重命名warp
9. /ewa trust <warp名字> <信任者名字> - 添加信任者, 信任者可用访问你设置为私有的warp
10. /ewa untrust <warp名字> <信任者名字> - 移除信任者, 信任者可用访问你设置为私有的warp
11. /ewa info <名字> - 查看warp详细信息
12. /ewa limit - 查询创建限制和价格
13. /ewa list <玩家名字> - 查看该玩家的warp
14. /ewa listall - 查看所有warp
15. /ewa tp <名字> - 传送到warp

## 权限

```yaml
ewarp.admin:
  default: op
  description: 允许使用插件管理指令

ewarp.use:
  default: true
  description: 允许查看, 使用对自己可见的warp(自己创建/信任)

ewarp.edit:
  default: true
  description: |-
    允许新建, 修改, 删除, 变更可见性
    仅对自己创建的warp可用

ewarp.trust:
  default: true
  description: |-
    允许添加和删除信任者
    指令 trust, untrust, padd, pdel 用到此权限

ewarp.limit.*:
  default: op
  description: 允许创建无限数量的warp

ewarp.bypass.price:
  default: op
  description: 允许跳过经济消耗
ewarp.bypass.warmup:
  default: op
  description: 允许跳过传送预热
ewarp.bypass.cooldown:
  default: op
  description: 允许跳过传送冷却
```

## 占位符

### 对应玩家的

1. %ewarp_max% - 创建上限(若不受限制则返回-1)
2. %ewarp_max_parse% - 创建上限(若不受限制则返回'∞')
3. %ewarp_exist% - 已创建数量
4. %ewarp_price% - 下一个创建的价格
5. %ewarp_list% - 玩家已创建的warp列表

### 对应warp名字的

1. %ewarp_{name}_owner% - 对应warp的创建者
2. %ewarp_{name}_price% - 对应warp的创建价格
3. %ewarp_{name}_x% - 对应warp的x坐标
4. %ewarp_{name}_y% - 对应warp的y坐标
5. %ewarp_{name}_z% - 对应warp的z坐标
6. %ewarp_{name}_pitch% - 对应warp的pitch(俯仰角)
7. %ewarp_{name}_yaw% - 对应warp的yaw(方向)
8. %ewarp_{name}_public% - 对应warp的状态
9. %ewarp_{name}_public_{1}_{2}% - 对应warp的状态, 若公开则返回{1}否则{2}，此处的{1}和{2}皆可自定义
10. %ewarp_{name}_world% - 对应warp的世界名字
11. %ewarp_{name}_world_parse% - 对应warp的multiverse世界名字
12. %ewarp_{name}_date% - 对应warp的创建日期
13. %ewarp_{name}_date_{日期格式}% - 对应warp的创建日期并格式化，默认格式 yyyy.MM.dd HH:mm:ss
14. %ewarp_{name}_trusts% - 对应warp的信任者列表，默认分隔符','
15. %ewarp_{name}_trusts_{分隔符}% - 对应warp的信任者列表(自定义分隔符)

## 计划中

1. 支持更多的papi
2. 支持根据私有和公开展示不同的颜色
3. 通过github检查更新

## bstats

![bstats](https://bstats.org/signatures/bukkit/EWarp.svg)
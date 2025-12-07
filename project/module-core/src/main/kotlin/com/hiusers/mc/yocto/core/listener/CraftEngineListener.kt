package com.hiusers.mc.yocto.core.listener

import com.hiusers.mc.yocto.core.Yocto.bypass
import com.hiusers.mc.yocto.core.Yocto.inProtectWorld
import net.momirealms.craftengine.bukkit.api.event.FurnitureAttemptBreakEvent
import net.momirealms.craftengine.bukkit.api.event.FurnitureAttemptPlaceEvent
import net.momirealms.craftengine.bukkit.api.event.FurnitureBreakEvent
import net.momirealms.craftengine.bukkit.api.event.FurniturePlaceEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.OptionalEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * Craft-Engine 家具保护监听器
 * 在 Yocto 保护的世界中禁止放置和破坏 craft-engine 的家具
 *
 * @author iiabc
 * @since 2025/1/XX
 */
object CraftEngineListener {

    /**
     * 监听家具尝试放置事件（更早的事件，在放置之前触发）
     * 如果玩家没有权限，则取消事件
     */
    @SubscribeEvent(
        bind = "net.momirealms.craftengine.bukkit.api.event.FurnitureAttemptPlaceEvent",
        priority = EventPriority.LOWEST,
        ignoreCancelled = false
    )
    fun onFurnitureAttemptPlace(event: OptionalEvent) {
        val placeEvent = event.get<FurnitureAttemptPlaceEvent>()
        val player = placeEvent.player()
        val location = placeEvent.location()

        // 检查家具位置是否在受保护的世界
        val world = location.world ?: return
        if (!world.inProtectWorld()) {
            return
        }

        // 检查玩家是否有权限绕过（需要硬权限检查，即需要潜行）
        if (player.bypass(true)) {
            return
        }

        // 取消事件，阻止放置
        placeEvent.setCancelled(true)
    }

    /**
     * 监听家具放置事件（实际放置时触发）
     * 如果玩家没有权限，则取消事件并销毁已放置的家具
     */
    @SubscribeEvent(
        bind = "net.momirealms.craftengine.bukkit.api.event.FurniturePlaceEvent",
        priority = EventPriority.LOWEST,
        ignoreCancelled = false
    )
    fun onFurniturePlace(event: OptionalEvent) {
        val placeEvent = event.get<FurniturePlaceEvent>()
        val player = placeEvent.player()
        val location = placeEvent.location()

        // 检查家具位置是否在受保护的世界
        val world = location.world ?: return
        if (!world.inProtectWorld()) {
            return
        }

        // 检查玩家是否有权限绕过（需要硬权限检查，即需要潜行）
        if (player.bypass(true)) {
            return
        }

        // 取消事件，这会触发 craft-engine 销毁已放置的家具
        placeEvent.setCancelled(true)
    }

    /**
     * 监听家具尝试破坏事件（更早的事件，在破坏之前触发）
     * 如果玩家没有权限，则取消事件
     */
    @SubscribeEvent(
        bind = "net.momirealms.craftengine.bukkit.api.event.FurnitureAttemptBreakEvent",
        priority = EventPriority.LOWEST,
        ignoreCancelled = false
    )
    fun onFurnitureAttemptBreak(event: OptionalEvent) {
        val breakEvent = event.get<FurnitureAttemptBreakEvent>()
        val player = breakEvent.player()
        val location = breakEvent.location()


        // 检查家具位置是否在受保护的世界
        val world = location.world ?: return
        if (!world.inProtectWorld()) {
            return
        }

        // 检查玩家是否有权限绕过（需要硬权限检查，即需要潜行）
        if (player.bypass(true)) {
            return
        }

        // 取消事件，阻止破坏
        breakEvent.setCancelled(true)
    }

    /**
     * 监听家具破坏事件（实际破坏时触发）
     * 如果玩家没有权限，则取消事件
     */
    @SubscribeEvent(
        bind = "net.momirealms.craftengine.bukkit.api.event.FurnitureBreakEvent",
        priority = EventPriority.LOWEST,
        ignoreCancelled = false
    )
    fun onFurnitureBreak(event: OptionalEvent) {
        val breakEvent = event.get<FurnitureBreakEvent>()
        val player = breakEvent.player()
        val location = breakEvent.location()


        // 检查家具位置是否在受保护的世界
        val world = location.world ?: return
        if (!world.inProtectWorld()) {
            return
        }

        // 检查玩家是否有权限绕过（需要硬权限检查，即需要潜行）
        if (player.bypass(true)) {
            return
        }

        // 取消事件，阻止破坏
        breakEvent.setCancelled(true)
    }

}

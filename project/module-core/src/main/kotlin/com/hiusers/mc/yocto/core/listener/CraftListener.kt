package com.hiusers.mc.yocto.core.listener

import com.hiusers.mc.yocto.core.Yocto
import com.hiusers.mc.yocto.core.Yocto.inProtectWorld
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author iiabc
 * @since 2025/8/26 14:33
 */
object CraftListener {

    /**
     * 禁止合成物品
     */
    @SubscribeEvent
    fun event(ev: CraftItemEvent) {
        if (!Yocto.allowCraft) {
            if (!ev.whoClicked.inProtectWorld()) return
            ev.isCancelled = true
        }
    }

    /**
     * 禁止合成
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun event(ev: InventoryClickEvent) {
        if (!Yocto.allowCraft) {
            if (!ev.whoClicked.inProtectWorld()) return
            ev.isCancelled = ev.clickedInventory?.type == InventoryType.CRAFTING
        }
    }

    /**
     * 禁止合成
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun event(ev: InventoryDragEvent) {
        if (!Yocto.allowCraft) {
            if (!ev.whoClicked.inProtectWorld()) return
            ev.isCancelled = ev.inventory.type == InventoryType.CRAFTING && ev.rawSlots.any { it < 5 }
        }
    }

}
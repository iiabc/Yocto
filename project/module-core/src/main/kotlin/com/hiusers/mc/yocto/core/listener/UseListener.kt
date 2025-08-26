package com.hiusers.mc.yocto.core.listener

import com.hiusers.mc.yocto.core.Yocto
import com.hiusers.mc.yocto.core.Yocto.bypass
import com.hiusers.mc.yocto.core.Yocto.inProtectWorld
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Arrow
import org.bukkit.entity.FishHook
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerInteractEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author iiabc
 * @since 2025/8/26 14:35
 */
object UseListener {

    /**
     * 禁止打开界面
     */
    @SubscribeEvent
    fun event(ev: InventoryOpenEvent) {
        if (Yocto.inventoryBlock && ev.player.inProtectWorld()) {
            val inventoryType = Yocto.inventoryType
            ev.isCancelled = (inventoryType.isEmpty() || ev.inventory.type.name in inventoryType)
        }
    }

    /**
     * 禁止方块交互
     */
    @SubscribeEvent
    fun event(ev: PlayerInteractEvent) {
        if (!Yocto.interactBlock) return
        if (!ev.player.inProtectWorld()) return
        if ((ev.action == Action.RIGHT_CLICK_BLOCK || ev.action == Action.LEFT_CLICK_BLOCK) && !ev.player.bypass()) {
            val type = ev.clickedBlock!!.type.name
            val interactType = Yocto.interactType
            if (interactType.isEmpty() || interactType.any {
                    if (it.endsWith("?")) it.substring(
                        0,
                        it.length - 1
                    ) in type else it == type
                }) {
                ev.isCancelled = true
            }
        }
    }

    /**
     * 禁止鱼竿移动盔甲架
     * 禁止创造模式射出的弓箭在世界停留
     */
    @SubscribeEvent
    fun event(ev: ProjectileHitEvent) {
        if (!ev.entity.inProtectWorld()) return
        if (ev.entity is FishHook && ev.hitEntity is ArmorStand) {
            if ("PROJECTILE_FISH" in Yocto.blockFeatures) {
                ev.entity.remove()
            }
        }
        if (ev.entity is Arrow && (ev.entity as Arrow).pickupStatus != AbstractArrow.PickupStatus.ALLOWED) {
            if ("PROJECTILE_ARROW" in Yocto.blockFeatures) {
                ev.entity.remove()
            }
        }
    }

}
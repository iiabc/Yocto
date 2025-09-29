package com.hiusers.mc.yocto.core.listener

import com.hiusers.mc.yocto.core.Yocto
import com.hiusers.mc.yocto.core.Yocto.bypass
import com.hiusers.mc.yocto.core.Yocto.inProtectWorld
import org.bukkit.Material
import org.bukkit.entity.Hanging
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.*
import org.bukkit.event.entity.*
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.raid.RaidTriggerEvent
import org.bukkit.util.Vector
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.platform.util.attacker

/**
 * @author iiabc
 * @since 2025/8/26 14:43
 */
object WorldListener {

    @SubscribeEvent
    fun event(e: EntityBreedEvent) {
        if ("BREED" in Yocto.blockFeatures) {
            if (!e.entity.inProtectWorld()) return
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun event(e: LeavesDecayEvent) {
        if ("LEAVES_DECAY" in Yocto.blockFeatures) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun event(e: EntityChangeBlockEvent) {
        if ("ENTITY_CHANGE_BLOCK" in Yocto.blockFeatures && e.entity is LivingEntity) {
            if (!e.entity.inProtectWorld()) return
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun event(e: PlayerInteractEvent) {
        if ("FARMLAND_PHYSICAL" in Yocto.blockFeatures && e.action == Action.PHYSICAL && e.clickedBlock!!.type == Material.FARMLAND) {
            if (!e.player.inProtectWorld()) return
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun event(e: EntityInteractEvent) {
        if ("FARMLAND_PHYSICAL" in Yocto.blockFeatures && e.block.type == Material.FARMLAND) {
            if (!e.entity.inProtectWorld()) return
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun event(e: HangingBreakEvent) {
        if ("HANGING_BREAK" in Yocto.blockFeatures && e.cause != HangingBreakEvent.RemoveCause.ENTITY) {
            if (!e.entity.inProtectWorld()) return
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun event(e: HangingBreakByEntityEvent) {
        if ("HANGING_BREAK" in Yocto.blockFeatures && e.remover?.bypass(true) == false) {
            if (!e.entity.inProtectWorld()) return
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun event(e: PlayerInteractEntityEvent) {
        if ("HANGING_BREAK" in Yocto.blockFeatures && !e.player.bypass(true) && e.rightClicked is Hanging) {
            if (!e.player.inProtectWorld()) return
            e.isCancelled = true
        }
    }

    @SubscribeEvent
    fun event(e: EntityDamageByEntityEvent) {
        val player = e.attacker ?: return
        if ("HANGING_BREAK" in Yocto.blockFeatures && !player.bypass(true) && e.entity is Hanging) {
            if (!player.inProtectWorld()) return
            e.isCancelled = true
        } else if ("DAMAGE_ENTITY" in Yocto.blockFeatures) {
            // 获取造成伤害的实体
            val attacker = e.attacker ?: return
            if (attacker is Player && attacker.inProtectWorld() && !player.bypass()) {
                e.isCancelled = true
            }
        }
    }

    @SubscribeEvent
    fun event(ev: PlayerTeleportEvent) {
        if (!ev.player.inProtectWorld()) return
        ev.isCancelled = ev.cause.name in Yocto.blockTeleport
    }

    @SubscribeEvent
    fun event(ev: EntityExplodeEvent) {
        if ("ENTITY_EXPLODE" in Yocto.blockFeatures) {
            if (!ev.entity.inProtectWorld()) return
            ev.blockList().clear()
        }
    }

    @SubscribeEvent
    fun event(ev: BlockExplodeEvent) {
        if ("BLOCK_EXPLODE" in Yocto.blockFeatures) {
            if (!ev.block.world.inProtectWorld()) return
            ev.blockList().clear()
        }
    }

    @SubscribeEvent
    fun event(ev: RaidTriggerEvent) {
        if ("RAID" in Yocto.blockFeatures) {
            if (!ev.player.inProtectWorld()) return
            ev.isCancelled = true
        }
    }

    @SubscribeEvent
    fun event(ev: BlockSpreadEvent) {
        if ("SPREAD" in Yocto.blockFeatures) {
            if (!ev.block.world.inProtectWorld()) return
            ev.isCancelled = true
        }
    }

    @SubscribeEvent
    fun event(ev: BlockGrowEvent) {
        if ("GROW" in Yocto.blockFeatures) {
            if (!ev.block.world.inProtectWorld()) return
            ev.isCancelled = true
        }
    }

    @SubscribeEvent
    fun event(ev: PlayerMoveEvent) {
        if (!ev.player.inProtectWorld()) return
        val to = ev.to!!
        if (Yocto.voidProtect && (ev.from.x != to.x || ev.from.y != to.y || ev.from.z != to.z)) {
            // 使用世界最小高度作为虚空阈值（1.18+），老版本回退为 0
            val minY = try { ev.player.world.minHeight } catch (_: Throwable) { 0 }
            // 仅当玩家向下坠落并低于最小高度时触发
            if (to.y < ev.from.y && to.blockY < minY + 1) {
                // 直接传送而不取消事件，避免被“送回原地”的橡皮筋体验
                val p = ev.player
                p.velocity = Vector(0, 0, 0)
                p.fallDistance = 0f
                p.teleport(p.world.spawnLocation)
                // 再次归零以防某些实现中落地判定在下一 tick 发生
                submit {
                    p.fallDistance = 0f
                }
            }
        }
    }

}
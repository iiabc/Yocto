package com.hiusers.mc.yocto.core.listener

import com.hiusers.mc.yocto.core.Yocto
import com.hiusers.mc.yocto.core.Yocto.bypass
import com.hiusers.mc.yocto.core.Yocto.inProtectWorld
import org.bukkit.Material
import org.bukkit.entity.*
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
        val damager = e.damager
        val victim = e.entity
        
        // 处理展示物破坏
        if ("HANGING_BREAK" in Yocto.blockFeatures && !damager.bypass(true) && victim is Hanging) {
            if (!damager.inProtectWorld()) return
            e.isCancelled = true
            return
        }
        
        // 处理 DAMAGE_ENTITY 保护（包括玩家对玩家、弓箭、药水等所有伤害）
        if ("DAMAGE_ENTITY" in Yocto.blockFeatures) {
            // 获取实际造成伤害的玩家（可能是直接攻击者，也可能是投射物的发射者）
            val attackerPlayer: Player? = when (damager) {
                is Player -> damager
                is Arrow -> {
                    // 弓箭伤害：检查发射者是否为玩家
                    val shooter = damager.shooter
                    if (shooter is Player) shooter else null
                }
                is ThrownPotion -> {
                    // 投掷药水伤害：检查投掷者是否为玩家
                    val thrower = damager.shooter
                    if (thrower is Player) thrower else null
                }
                is AreaEffectCloud -> {
                    // 药水云伤害：检查来源
                    val source = damager.source
                    if (source is Player) source else null
                }
                else -> null
            }
            
            // 如果攻击者是玩家，检查是否需要保护
            if (attackerPlayer != null) {
                // 检查攻击者是否在受保护的世界且有权限
                if (attackerPlayer.inProtectWorld() && !attackerPlayer.bypass()) {
                    // 如果受害者是玩家，需要检查受害者是否也在受保护的世界
                    if (victim is Player) {
                        if (victim.inProtectWorld()) {
                            e.isCancelled = true
                        }
                    } else {
                        // 如果受害者不是玩家，也保护（原有逻辑：禁止玩家伤害所有实体）
                        e.isCancelled = true
                    }
                }
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
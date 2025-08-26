package com.hiusers.mc.yocto.core.listener

import com.hiusers.mc.yocto.core.Yocto
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.colored

/**
 * @author iiabc
 * @since 2025/8/26 14:48
 */
object MessageListener {

    /**
     * 进入提示
     */
    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        val message = Yocto.joinMessage
        if (Yocto.joinMessage.isEmpty()) {
            e.joinMessage = null
        } else {
            e.joinMessage = message.colored().replace("@p", e.player.name)
        }
    }

    /**
     * 离开提示
     */
    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        val message = Yocto.quitMessage
        if (Yocto.quitMessage.isEmpty()) {
            e.quitMessage = null
        } else {
            e.quitMessage = message.colored().replace("@p", e.player.name)
        }
    }

}
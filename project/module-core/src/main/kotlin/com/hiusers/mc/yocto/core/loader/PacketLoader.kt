package com.hiusers.mc.yocto.core.loader

import com.github.retrooper.packetevents.PacketEvents
import com.hiusers.mc.yocto.core.listener.PacketEventListener
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.platform.util.bukkitPlugin

/**
 * @author iiabc
 * @since 2025/8/26 15:53
 */
object PacketLoader {

    @Awake(LifeCycle.LOAD)
    fun load() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(bukkitPlugin))
        PacketEvents.getAPI().load()
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        PacketEvents.getAPI().terminate()
    }

    @Awake(LifeCycle.ENABLE)
    fun enable() {
        PacketEvents.getAPI().init()

        PacketEvents.getAPI().eventManager.registerListener(PacketEventListener())
    }

}
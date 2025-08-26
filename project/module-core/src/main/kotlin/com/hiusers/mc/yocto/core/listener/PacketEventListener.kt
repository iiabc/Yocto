package com.hiusers.mc.yocto.core.listener

import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage
import com.hiusers.mc.yocto.core.Yocto
import com.hiusers.mc.yocto.core.Yocto.inProtectWorld
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

/**
 * @author iiabc
 * @since 2025/8/26 14:29
 */
class PacketEventListener : PacketListenerAbstract() {

    override fun onPacketSend(event: PacketSendEvent) {
        if ("ACHIEVEMENT" !in Yocto.blockFeatures) return
        val player = event.getPlayer<Player>() ?: return
        if (!player.inProtectWorld()) return

        when (event.packetType) {
            // 禁止成就相关
            PacketType.Play.Server.UPDATE_ADVANCEMENTS -> {
                event.isCancelled = true
            }

            // 禁止成就相关
            PacketType.Play.Server.SYSTEM_CHAT_MESSAGE -> {
                val wrapper = WrapperPlayServerSystemChatMessage(event)
                val message = wrapper.message
                // 检查消息内容是否包含成就相关文本
                if (isAdvancementMessage(message)) {
                    event.isCancelled = true
                }
            }

            // 禁止成就相关
            PacketType.Play.Server.CHAT_MESSAGE -> {
                val wrapper = WrapperPlayServerChatMessage(event)
                val message = wrapper.message
                // 检查消息内容是否包含成就相关文本
                if (isAdvancementMessage(message.chatContent)) {
                    event.isCancelled = true
                }
            }

            // 阻止服务器发送配方声明
            PacketType.Play.Server.DECLARE_RECIPES -> {
                event.isCancelled = !Yocto.allowRecipe
            }
            // 阻止添加配方到配方书
            PacketType.Play.Server.RECIPE_BOOK_ADD -> {
                event.isCancelled = !Yocto.allowRecipe
            }
        }
    }

    private fun isAdvancementMessage(component: Component?): Boolean {
        if (component == null) return false
        val text = component.toString()
        // 检查是否包含成就相关的翻译键或文本
        return text.contains("advancement.") ||
                text.contains("achievement.") ||
                text.contains("has made the advancement") ||
                text.contains("has completed the challenge")
    }

    override fun onPacketReceive(event: PacketReceiveEvent) {
        val player = event.getPlayer<Player>() ?: return
        if (!player.inProtectWorld()) return

        when (event.packetType) {
            // 阻止玩家发送配方制作请求
            PacketType.Play.Client.CRAFT_RECIPE_REQUEST -> {
                event.isCancelled = !Yocto.allowRecipe
            }

            // 阻止玩家设置显示的配方
            PacketType.Play.Client.SET_DISPLAYED_RECIPE -> {
                event.isCancelled = !Yocto.allowRecipe
            }
        }
    }

}
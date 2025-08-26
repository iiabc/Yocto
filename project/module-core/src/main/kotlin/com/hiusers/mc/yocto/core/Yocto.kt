package com.hiusers.mc.yocto.core

import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration

object Yocto {

    @Config("config.yml", autoReload = true)
    lateinit var config: Configuration
        private set

    @ConfigNode("worlds")
    var worlds = listOf<String>()

    @ConfigNode("message.join")
    var joinMessage = ""

    @ConfigNode("message.quit")
    var quitMessage = ""

    @ConfigNode("allow-craft")
    var allowCraft = true

    @ConfigNode("allow-recipe")
    var allowRecipe = true

    @ConfigNode("void-protect")
    var voidProtect = true

    @ConfigNode("inventory.block")
    var inventoryBlock = true

    @ConfigNode("inventory.type")
    var inventoryType = listOf<String>()

    @ConfigNode("interact.block")
    var interactBlock = true

    @ConfigNode("interact.type")
    var interactType = listOf<String>()

    @ConfigNode("block-features")
    var blockFeatures = listOf<String>()

    @ConfigNode("block-teleport")
    var blockTeleport = listOf<String>()

    fun Entity.bypass(hard: Boolean = false): Boolean {
        return this !is Player || (isOp || hasPermission("yocto.bypass")) && gameMode == GameMode.CREATIVE && (!hard || isSneaking)
    }

    /**
     * 是否在受保护的世界
     */
    fun Entity.inProtectWorld(): Boolean {
        return worlds.isEmpty() || world.name in worlds
    }

    /**
     * 是否在受保护的世界
     */
    fun World.inProtectWorld(): Boolean {
        return worlds.isEmpty() || name in worlds
    }

}
package com.example

import com.example.network.Networking
import com.example.screen.ScreenHandlerTypes
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object TemplateMod : ModInitializer {
    private val logger = LoggerFactory.getLogger("template-mod")

    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        logger.info("Hello Fabric world!")
        ModItem.initialize()
        ModBlocks.initialize()
        EntityTypes.init()
        ScreenHandlerTypes.init()

        Networking.register()

        //ItemStorage.SIDED.registerForBlockEntity(RefineryEntity::inventoryProvider, ::RefineryEntity)
    }
}
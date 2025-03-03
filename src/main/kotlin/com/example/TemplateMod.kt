package com.example

import com.example.registries.ModNetworking
import com.example.registries.ModRecipeSerializers
import com.example.registries.ModRecipeTypes
import com.example.registries.ModBlocks
import com.example.registries.ModEntityTypes
import com.example.registries.ModItems
import com.example.registries.ModScreenHandlers
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object TemplateMod : ModInitializer {
    private val logger = LoggerFactory.getLogger("template-mod")

    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        logger.info("Hello Fabric world!")
        ModItems.initialize()
        ModBlocks.initialize()
        ModEntityTypes.init()
        ModScreenHandlers.init()
        ModRecipeTypes.init()
        ModRecipeSerializers.init()

        ModNetworking.register()

        //ItemStorage.SIDED.registerForBlockEntity(RefineryEntity::inventoryProvider, ::RefineryEntity)
    }

    fun id(path: String): Identifier = Identifier.of("template-mod", path)
}
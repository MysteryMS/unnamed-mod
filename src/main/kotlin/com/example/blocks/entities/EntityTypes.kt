package com.example.blocks.entities

import com.example.ModBlocks
import com.example.blocks.entities.refinery.RefineryEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier


object EntityTypes {
    lateinit var REFINERY: BlockEntityType<RefineryEntity>

    fun <T : BlockEntity> register(
        name: String,
        entityFactory: FabricBlockEntityTypeBuilder.Factory<out T>,
        vararg blocks: Block
    ): BlockEntityType<T> {
        val identifier = Identifier.of("template-mod", name)
        return Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            identifier,
            FabricBlockEntityTypeBuilder.create(entityFactory, *blocks).build()
        )
    }

    fun init() {
        REFINERY = register("refinery", ::RefineryEntity, ModBlocks.refinery)
    }

}

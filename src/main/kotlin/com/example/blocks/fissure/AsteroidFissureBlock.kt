package com.example.blocks.fissure

import com.example.registries.ModEntityTypes
import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class AsteroidFissureBlock(settings: Settings?) : BlockWithEntity(settings) {
    companion object {
        private val id = Identifier.of("template-mod", "asteroid_fissure_block")
        val key = RegistryKey.of(RegistryKeys.BLOCK, id)
        val settings = Settings.create().registryKey(key)
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> {
        return createCodec(::AsteroidFissureBlock)
    }

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity {
        return AsteroidFissureEntity(pos, state)
    }

    override fun <T : BlockEntity?> getTicker(
        world: World?,
        state: BlockState?,
        type: BlockEntityType<T>?
    ): BlockEntityTicker<T>? {
        return validateTicker(type, ModEntityTypes.ASTEROID_FISSURE, AsteroidFissureEntity.Companion::tick)
    }

}

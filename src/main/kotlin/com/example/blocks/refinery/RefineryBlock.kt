package com.example.blocks.refinery

import com.example.registries.ModEntityTypes
import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class RefineryBlock(settings: Settings) : BlockWithEntity(settings) {

    override fun getCodec(): MapCodec<out BlockWithEntity> {
        return createCodec(::RefineryBlock)
    }

    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? {
        return ModEntityTypes.REFINERY.instantiate(pos, state)
    }

    override fun <T : BlockEntity?> getTicker(
        world: World?,
        state: BlockState?,
        type: BlockEntityType<T>?
    ): BlockEntityTicker<T>? {
        if (world == null) return null
        if (world.isClient) return null

        return validateTicker(
            type,
            ModEntityTypes.REFINERY
        ) { w: World, blockPos: BlockPos, blockState: BlockState, refineryEntity: RefineryEntity ->
            refineryEntity.tick(w, blockPos, blockState, refineryEntity)
        }
    }

    override fun onUse(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hit: BlockHitResult?
    ): ActionResult {
        val screen = state?.createScreenHandlerFactory(world, pos)
        val block = world?.getBlockEntity(pos)

        if (block is RefineryEntity) {
            screen?.let {
                player?.openHandledScreen(block)
            }
        }

        return ActionResult.SUCCESS;
    }

    // TODO: add loot on destroy

}

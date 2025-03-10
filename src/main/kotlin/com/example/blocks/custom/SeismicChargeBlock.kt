package com.example.blocks.custom

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView


class SeismicChargeBlock(settings: Settings?) : Block(settings) {
    companion object {
        val FACING = HorizontalFacingBlock.FACING
        val id = Identifier.of("template-mod", "seismic_charge")
        val key = RegistryKey.of(RegistryKeys.BLOCK, id)
    }

    init {
        defaultState = stateManager.defaultState.with(FACING, Direction.NORTH)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return super.getPlacementState(ctx)!!.with(Properties.HORIZONTAL_FACING, ctx.horizontalPlayerFacing.opposite)
    }

    override fun rotate(state: BlockState?, rotation: BlockRotation?): BlockState {
        return state!!.rotate(BlockRotation.COUNTERCLOCKWISE_90)

    }

    override fun mirror(state: BlockState?, mirror: BlockMirror?): BlockState {
        return super.mirror(state, mirror)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(FACING)
    }

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return VoxelShapes.cuboid(0.2, 0.0, 0.2, 0.8, 0.4,0.8)
    }
}
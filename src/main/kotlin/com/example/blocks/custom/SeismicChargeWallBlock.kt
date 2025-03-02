package com.example.blocks.custom

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView


class SeismicChargeWallBlock(settings: Settings) : Block(settings) {
    companion object {
        val id = Identifier.of("template-mod", "seismic_charge_wall")
        val key = RegistryKey.of(RegistryKeys.BLOCK, id)
        val settings = Settings.create().registryKey(key)
    }

    private val voxelShapes = mapOf(
        Direction.SOUTH to VoxelShapes.cuboid(0.2, 0.3, 0.0, 0.8, 0.8,0.5),
        Direction.WEST to VoxelShapes.cuboid(0.5, 0.3, 0.2, 1.0, 0.8,0.8),
        Direction.EAST to VoxelShapes.cuboid(0.0, 0.3, 0.2, 0.5, 0.8,0.8),
        Direction.NORTH to VoxelShapes.cuboid(0.2, 0.3, 0.5, 0.8, 0.8,1.0)
    )

    override fun getCodec(): MapCodec<out Block> {
        return createCodec(::SeismicChargeWallBlock)
    }

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        val facing = state?.get(Properties.FACING) ?: Direction.NORTH
        return voxelShapes[facing] ?: VoxelShapes.empty()
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(Properties.FACING)
    }
}
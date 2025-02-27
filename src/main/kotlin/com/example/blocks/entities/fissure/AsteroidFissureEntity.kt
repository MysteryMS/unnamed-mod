package com.example.blocks.entities.fissure

import com.example.blocks.entities.EntityTypes
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class AsteroidFissureEntity(pos: BlockPos?, state: BlockState?) :
    BlockEntity(EntityTypes.ASTEROID_FISSURE, pos, state) {

    var elapsed = 0

    companion object {
        @JvmStatic
        fun tick(world: World, pos: BlockPos, state: BlockState, entity: AsteroidFissureEntity) {
            entity.elapsed++

            if (entity.elapsed <= 20 * 1) return
            entity.elapsed = 0

            val directions = listOf(pos.up(), pos.down(), pos.east(), pos.west(), pos.north(), pos.south())

            for (dir in directions) {
                val block = world.getBlockState(dir).block
                //println(block)
            }
        }
    }


}
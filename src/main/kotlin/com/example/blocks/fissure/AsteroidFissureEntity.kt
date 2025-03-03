package com.example.blocks.fissure

import com.example.EntityTypes
import com.example.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class AsteroidFissureEntity(pos: BlockPos?, state: BlockState?) :
    BlockEntity(EntityTypes.ASTEROID_FISSURE, pos, state) {

    var elapsed = 0
    var ticksToDetonate = 100
    var detonateStarted = false
    var playerPlaced: PlayerEntity? = null

    companion object {
        @JvmStatic
        fun tick(world: World, pos: BlockPos, state: BlockState, entity: AsteroidFissureEntity) {
            entity.elapsed++

            if (entity.elapsed <= 20 * 1) return
            entity.elapsed = 0

            if (entity.detonateStarted) {
                entity.ticksToDetonate--
            }

            val directions = listOf(pos.up(), pos.down(), pos.east(), pos.west(), pos.north(), pos.south())

            for (dir in directions) {
                val block = world.getBlockState(dir).block
                if (block == ModBlocks.seismiscChargeWallBlock || block == ModBlocks.seismiscChargeWallBlock) {
                    entity.detonateStarted = true
                }
                // println(block)
            }

            if (entity.detonateStarted) {
                if (entity.ticksToDetonate <= 0) {
                    println("detonou")
                    entity.detonateStarted = false
                    entity.ticksToDetonate = 100
                    return
                }

                val text = Text.literal((entity.ticksToDetonate / 20).toString())
                entity.playerPlaced?.sendMessage(text, true)
            }
        }
    }


}
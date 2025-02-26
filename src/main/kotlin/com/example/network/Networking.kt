package com.example.network

import com.example.ModBlocks
import com.example.blocks.entities.refinery.Outcomes
import com.example.blocks.entities.refinery.RefineryEntity
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.item.BlockItem
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.Identifier

object Networking {
    val REFINERY_OUTPUT_CHANGE = Identifier.of("template-mod", "refinery_output_change")

    fun register() {
        PayloadTypeRegistry.playC2S().register(Id(REFINERY_OUTPUT_CHANGE), RefineryOutputChange.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(Id(REFINERY_OUTPUT_CHANGE)) { p, ctx ->
            val payload = p as RefineryOutputChange

            println(payload)
            val entity = ctx.player().world.getBlockEntity(payload.blockPos) as RefineryEntity?
                    ?: return@registerGlobalReceiver

            val item = entity.inventory.getStack(0).item as BlockItem

            if (item.block == ModBlocks.metalChunk.block) {
                val outcome = Outcomes.metalChunk[p.outputItemIndex]
                entity.output = outcome
            }
        }
    }

}
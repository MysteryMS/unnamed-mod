package com.example.network

import com.example.ModBlocks
import com.example.blocks.entities.refinery.RefineryEntity
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.item.BlockItem
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.Identifier
import kotlin.jvm.optionals.getOrNull

object Networking {
    val REFINERY_OUTPUT_CHANGE: Identifier = Identifier.of("template-mod", "refinery_output_change")
    val REFINERY_SCREEN_UPDATE_PAYLOAD = Identifier.of("template-mod", "refinery_screen_update_payload")

    fun register() {
        PayloadTypeRegistry.playC2S().register(Id(REFINERY_OUTPUT_CHANGE), RefineryOutputChange.CODEC)
        PayloadTypeRegistry.playS2C().register(Id(REFINERY_SCREEN_UPDATE_PAYLOAD), RefineryScreenUpdatePayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(Id(REFINERY_OUTPUT_CHANGE)) { p, ctx ->
            val payload = p as RefineryOutputChange

            val entity = ctx.player().world.getBlockEntity(payload.blockPos) as RefineryEntity?
                    ?: return@registerGlobalReceiver

           entity.updateOutcomeItem(payload.outputItemIdentifier.getOrNull())
        }
    }

}
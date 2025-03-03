package com.example.registries

import com.example.blocks.refinery.RefineryEntity
import com.example.network.RefineryOutputChange
import com.example.network.RefineryScreenUpdatePayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.Identifier
import kotlin.jvm.optionals.getOrNull

object ModNetworking {
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
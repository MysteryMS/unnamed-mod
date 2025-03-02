package com.example

import com.example.network.Networking
import com.example.network.RefineryScreenUpdatePayload
import com.example.networking.extensions.execute
import com.example.screen.ScreenHandlerTypes
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.network.packet.CustomPayload.Id

object TemplateModClient : ClientModInitializer {
    override fun onInitializeClient() {
        HandledScreens.register(ScreenHandlerTypes.refineryScreenHandlerType, ::RefineryScreen)


        ClientPlayNetworking.registerGlobalReceiver(Id(Networking.REFINERY_SCREEN_UPDATE_PAYLOAD)) { packet, ctx ->
            val payload =  packet as RefineryScreenUpdatePayload? ?: return@registerGlobalReceiver
            payload.execute(ctx)
        }
    }
}
package com.example

import com.example.network.Networking.REFINERY_OUTPUT_CHANGE
import com.example.network.RefineryOutputChange
import com.example.screen.ScreenHandlerTypes
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.network.packet.CustomPayload.Id

object TemplateModClient : ClientModInitializer {
    override fun onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        HandledScreens.register(ScreenHandlerTypes.refineryScreenHandlerType, ::RefineryScreen)

    }
}
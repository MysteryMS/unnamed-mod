package com.example.registries

import com.example.network.RefineryScreenUpdatePayload
import com.example.screen.RefineryScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType

object ModScreenHandlers {
    lateinit var refineryScreenHandlerType: ScreenHandlerType<RefineryScreenHandler>

    fun <T : ScreenHandler, D : CustomPayload> register(
        name: String,
        factory: ExtendedScreenHandlerType.ExtendedFactory<T, D>,
        codec: PacketCodec<in RegistryByteBuf, D>
    ): ExtendedScreenHandlerType<T, D> {
        return Registry.register(Registries.SCREEN_HANDLER, name, ExtendedScreenHandlerType(factory, codec))
    }

    fun init() {
        refineryScreenHandlerType =
            register("refinery_scr_handler", ::RefineryScreenHandler, RefineryScreenUpdatePayload.CODEC)
    }
}
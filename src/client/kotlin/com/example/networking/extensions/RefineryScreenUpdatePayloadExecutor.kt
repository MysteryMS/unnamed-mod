package com.example.networking.extensions

import com.example.network.RefineryScreenUpdatePayload
import com.example.screen.RefineryScreenHandler
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

fun RefineryScreenUpdatePayload.execute(ctx:  ClientPlayNetworking.Context) {
    println("RefineryScreenUpdatePayload: $this")
    ctx.client().execute {
        val currentScreenHandler = ctx.player().currentScreenHandler
        if (currentScreenHandler is RefineryScreenHandler && currentScreenHandler.entity.pos == this.blockPos) {
            currentScreenHandler.payload = this
        }
    }
}
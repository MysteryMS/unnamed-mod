package com.example.network

import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.math.BlockPos

data class RefineryOutputChange(
    val blockPos: BlockPos,
    val outputItemIndex: Int
) : CustomPayload {
    companion object {
        val CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, RefineryOutputChange::blockPos,
            PacketCodecs.INTEGER, RefineryOutputChange::outputItemIndex,
            ::RefineryOutputChange
        )
    }
    override fun getId(): Id<out CustomPayload> {
        return Id(Networking.REFINERY_OUTPUT_CHANGE)
    }
}

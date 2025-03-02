package com.example.network

import net.minecraft.item.Item
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import java.util.*

data class RefineryOutputChange(
    val blockPos: BlockPos,
    val outputItemIdentifier: Optional<Identifier>
) : CustomPayload {
    companion object {
        val CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, RefineryOutputChange::blockPos,
            PacketCodecs.optional(Identifier.PACKET_CODEC), RefineryOutputChange::outputItemIdentifier,
            ::RefineryOutputChange
        )
    }
    override fun getId(): Id<out CustomPayload> {
        return Id(Networking.REFINERY_OUTPUT_CHANGE)
    }
}

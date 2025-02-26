package com.example.network

import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

data class BlockPosPayload(val pos: BlockPos) : CustomPayload {
    companion object {
        val PACKET_CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, BlockPosPayload::pos, ::BlockPosPayload)
    }

    override fun getId(): Id<out CustomPayload> {
        return Id(Identifier.of("template-mod", "block_pos"))
    }
}

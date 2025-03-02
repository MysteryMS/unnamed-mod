package com.example.network

import com.example.blocks.refinery.RefineryStatus
import com.example.registries.ModNetworking
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import java.util.*
import java.util.function.IntFunction
import java.util.function.ToIntFunction

data class RefineryScreenUpdatePayload(
    val blockPos: BlockPos,
//    val selectedRecipeId: Optional<NetworkRecipeId>,
    val selectedRecipeOutputs: Optional<List<ItemStack>>,
    val outputItemSelectedIdentifier: Optional<Identifier>,
    val processedItemPercent: Float,
    val status: RefineryStatus
): CustomPayload {
    companion object {
        val CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, RefineryScreenUpdatePayload::blockPos,
//            PacketCodecs.optional(NetworkRecipeId.PACKET_CODEC), RefineryScreenUpdatePayload::selectedRecipeId,
            PacketCodecs.optional(PacketCodecs.codec(ItemStack.CODEC.listOf())), RefineryScreenUpdatePayload::selectedRecipeOutputs,
            PacketCodecs.optional(Identifier.PACKET_CODEC), RefineryScreenUpdatePayload::outputItemSelectedIdentifier,
            PacketCodecs.FLOAT, RefineryScreenUpdatePayload::processedItemPercent,
            PacketCodecs.indexed({ RefineryStatus.entries[it] }, { it.ordinal }), RefineryScreenUpdatePayload::status,
            ::RefineryScreenUpdatePayload
        )
    }

    override fun getId(): Id<out CustomPayload>
        = Id(ModNetworking.REFINERY_SCREEN_UPDATE_PAYLOAD)
}
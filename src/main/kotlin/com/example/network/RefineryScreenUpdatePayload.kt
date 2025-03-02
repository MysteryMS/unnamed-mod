package com.example.network

import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.recipe.NetworkRecipeId
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeDisplayEntry
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import java.util.Optional

data class RefineryScreenUpdatePayload(
    val blockPos: BlockPos,
//    val selectedRecipeId: Optional<NetworkRecipeId>,
    val selectedRecipeOutputs: Optional<List<ItemStack>>,
    val outputItemSelectedIdentifier: Optional<Identifier>,
    val processedItemPercent: Float,
    val isBlocked: Boolean,
): CustomPayload {
    companion object {
        val CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, RefineryScreenUpdatePayload::blockPos,
//            PacketCodecs.optional(NetworkRecipeId.PACKET_CODEC), RefineryScreenUpdatePayload::selectedRecipeId,
            PacketCodecs.optional(PacketCodecs.codec(ItemStack.CODEC.listOf())), RefineryScreenUpdatePayload::selectedRecipeOutputs,
            PacketCodecs.optional(Identifier.PACKET_CODEC), RefineryScreenUpdatePayload::outputItemSelectedIdentifier,
            PacketCodecs.FLOAT, RefineryScreenUpdatePayload::processedItemPercent,
            PacketCodecs.BOOLEAN, RefineryScreenUpdatePayload::isBlocked,
            ::RefineryScreenUpdatePayload
        )
    }

    override fun getId(): Id<out CustomPayload>
        = Id(Networking.REFINERY_SCREEN_UPDATE_PAYLOAD)
}
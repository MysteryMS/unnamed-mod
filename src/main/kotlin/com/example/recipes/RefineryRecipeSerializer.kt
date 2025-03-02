package com.example.recipes

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer

class RefineryRecipeSerializer: RecipeSerializer<RefineryRecipe> {
    override fun codec(): MapCodec<RefineryRecipe> {
        return RecordCodecBuilder.mapCodec {
            it.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(RefineryRecipe::input),
                Codec.list(ItemStack.CODEC).fieldOf("outputs").forGetter(RefineryRecipe::outputs)
            ).apply(it) { ingredient, outputs ->
                RefineryRecipe(ingredient, outputs)
            }
        }
    }

    @Deprecated("super is deprecated")
    override fun packetCodec(): PacketCodec<RegistryByteBuf, RefineryRecipe> {
        return PacketCodec.tuple(
            Ingredient.PACKET_CODEC,
            RefineryRecipe::input,
            ItemStack.OPTIONAL_LIST_PACKET_CODEC,
            RefineryRecipe::outputs,
            ::RefineryRecipe
        )
    }

}
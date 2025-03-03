package com.example.recipes

import com.example.TemplateMod
import com.example.registries.ModRecipeSerializers
import com.example.registries.ModRecipeTypes
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.*
import net.minecraft.recipe.book.RecipeBookCategory
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.World

class RefineryRecipe(
    val input: Ingredient,
    val outputs: List<ItemStack>
): Recipe<RecipeInput> {
    init {
        if (outputs.size > 5) {
            throw IllegalArgumentException("The outputs for the RefineryRecipe should not be longer than 5")
        }
    }

    override fun matches(recipeInput: RecipeInput, world: World): Boolean {
        if (world.isClient) return false
        return input.test(recipeInput.getStackInSlot(0))
    }

    override fun getSerializer(): RecipeSerializer<out Recipe<RecipeInput>> {
        return ModRecipeSerializers.REFINERY_RECIPE_SERIALIZER
    }

    override fun getType(): RecipeType<out Recipe<RecipeInput>> {
        return ModRecipeTypes.REFINERY_RECIPE_TYPE
    }

    override fun getIngredientPlacement(): IngredientPlacement {
        return IngredientPlacement.NONE
    }


    override fun getRecipeBookCategory(): RecipeBookCategory? {
        return null
    }

    override fun craft(input: RecipeInput?, registries: RegistryWrapper.WrapperLookup?): ItemStack {
        return ItemStack(Items.COPPER_BULB)
    }

    override fun isIgnoredInRecipeBook(): Boolean = true


    class Type : RecipeType<RefineryRecipe> {
        companion object {
            val id = TemplateMod.id("refinery_recipe")
            val instance: Type = Type()
        }
    }
}
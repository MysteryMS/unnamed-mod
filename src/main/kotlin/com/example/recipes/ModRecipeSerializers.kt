package com.example.recipes

import com.example.TemplateMod
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModRecipeSerializers {
    lateinit var REFINERY_RECIPE_SERIALIZER: RecipeSerializer<RefineryRecipe>

    private fun <T: Recipe<*>> register(idPath: String, type: RecipeSerializer<T>): RecipeSerializer<T>
            = Registry.register(Registries.RECIPE_SERIALIZER, TemplateMod.id(idPath), type)!!

    fun init() {
        REFINERY_RECIPE_SERIALIZER =
            register<RefineryRecipe>("refinery_recipe",
                RefineryRecipeSerializer()
            )
    }
}
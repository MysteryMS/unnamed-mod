package com.example.recipes

import com.example.TemplateMod
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModRecipeTypes {
    lateinit var REFINERY_RECIPE_TYPE: RecipeType<RefineryRecipe>

    private fun <T: Recipe<*>> register(idPath: String, type: RecipeType<T>): RecipeType<T>
        = Registry.register(Registries.RECIPE_TYPE, TemplateMod.id(idPath), type)!!

    fun init() {
        REFINERY_RECIPE_TYPE = register("refinery_recipe", RefineryRecipe.Type.instance)
    }
}
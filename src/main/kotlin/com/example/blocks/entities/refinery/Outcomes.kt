package com.example.blocks.entities.refinery

import net.minecraft.item.Items

object Outcomes {
    val metalChunk = listOf(Items.COPPER_INGOT, Items.IRON_INGOT, Items.GOLD_INGOT)
    val metalChunkPercentages = mapOf(
        Items.COPPER_INGOT to 0.45,
        Items.IRON_INGOT to 0.40,
        Items.GOLD_INGOT to 0.10
    )
}
package com.example.blocks.custom

import com.example.registries.ModBlocks
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemUsageContext
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

class SeismicChargeBlockItem(block: Block?, settings: Settings?) : BlockItem(block, settings) {
    companion object {
        val id = Identifier.of("template-mod", "seismic_charge")
        val key = RegistryKey.of(RegistryKeys.ITEM, id)
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val direction = context.side

        if (context.side.axis.isHorizontal) {
            val wallBlock = ModBlocks.seismiscChargeWallBlock.defaultState.with(Properties.FACING, direction)
            if (this.place(ItemPlacementContext(context), wallBlock)) {
                return ActionResult.SUCCESS
            }
        }

        return super.useOnBlock(context)
    }
}
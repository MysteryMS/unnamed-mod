package com.example

import com.example.blocks.FissureBlock
import com.example.blocks.MetalChunkBlock
import com.example.blocks.RefineryBlock
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

object ModBlocks {
    val metalChunk = MetalChunkBlock
    val fissure = FissureBlock
    lateinit var refinery: RefineryBlock

    fun register(block: Block, key: RegistryKey<Block>, registerItem: Boolean = true): Block {
        if (registerItem) {
            val itemKey = RegistryKey.of(RegistryKeys.ITEM, key.value)
            val blockItem = BlockItem(block, Item.Settings().registryKey(itemKey))

            Registry.register(Registries.ITEM, itemKey, blockItem)
        }

        return Registry.register(
            Registries.BLOCK, key, block
        )
    }



    fun initialize() {
        metalChunk.block = Block(metalChunk.settings)

        fissure.block = Block(fissure.settings)
        val refinerySettings = AbstractBlock.Settings.create()
        val refineryId = Identifier.of("template-mod", "refinery")
        val refineryKey = RegistryKey.of(RegistryKeys.BLOCK, refineryId)
        refinerySettings.registryKey(refineryKey)

        refinery = RefineryBlock(refinerySettings)

        register(metalChunk.block!!, metalChunk.key, true)
        register(fissure.block!!, fissure.key, true)

        register(refinery, RegistryKey.of(RegistryKeys.BLOCK, refineryId))


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register { entries ->
            entries.add(metalChunk.block)
            entries.add(fissure.block)
            entries.add(refinery)
        }
    }
}
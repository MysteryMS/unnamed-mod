package com.example.registries

import com.example.blocks.MetalChunkBlock
import com.example.blocks.custom.SeismicChargeBlock
import com.example.blocks.custom.SeismicChargeBlockItem
import com.example.blocks.custom.SeismicChargeWallBlock
import com.example.blocks.fissure.AsteroidFissureBlock
import com.example.blocks.refinery.RefineryBlock
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

object ModBlocks {
    val metalChunk = MetalChunkBlock
    lateinit var fissure: AsteroidFissureBlock
    lateinit var refinery: RefineryBlock
    lateinit var seismicCharge: SeismicChargeBlock
    lateinit var seismiscChargeWallBlock: SeismicChargeWallBlock

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
        fissure = AsteroidFissureBlock(AsteroidFissureBlock.settings)
        seismicCharge = SeismicChargeBlock(
            AbstractBlock.Settings
                .create()
                .registryKey(SeismicChargeBlock.key)
        )
        seismiscChargeWallBlock = SeismicChargeWallBlock(SeismicChargeWallBlock.settings)

        val refinerySettings = AbstractBlock.Settings.create()
        val refineryId = Identifier.of("template-mod", "refinery")
        val refineryKey = RegistryKey.of(RegistryKeys.BLOCK, refineryId)
        refinerySettings.registryKey(refineryKey)

        refinery = RefineryBlock(refinerySettings)

        register(metalChunk.block!!, metalChunk.key, true)
        register(refinery, RegistryKey.of(RegistryKeys.BLOCK, refineryId))
        register(fissure, AsteroidFissureBlock.key)
        register(seismicCharge, RegistryKey.of(RegistryKeys.BLOCK, SeismicChargeBlockItem.id), false)


        val seismicChargeBlockItem =
            SeismicChargeBlockItem(seismicCharge, Item.Settings().registryKey(SeismicChargeBlockItem.key))

        Registry.register(Registries.ITEM, SeismicChargeBlockItem.id, seismicChargeBlockItem)

        register(seismiscChargeWallBlock, SeismicChargeWallBlock.key, false)


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register { entries ->
            entries.add(metalChunk.block)
            entries.add(fissure)
            entries.add(refinery)
            entries.add(seismicCharge)
        }
    }
}
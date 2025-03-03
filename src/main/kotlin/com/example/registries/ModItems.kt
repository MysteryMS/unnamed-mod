package com.example.registries

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

object ModItems {
    val METAL_CHUNK_KEY = RegistryKey.of(
        RegistryKeys.ITEM,
        Identifier.of("template-mod", "metal_chunk")
    )
    val METAL_CHUNK_BLOCK_KEY =
        RegistryKey.of(RegistryKeys.ITEM, Identifier.of("template-mod", "metal_chunk_block"))
    val metalChunkItem = Item.Settings().registryKey(METAL_CHUNK_KEY)
    lateinit var metalChunk: Item

    fun register(item: Item, registryKey: RegistryKey<Item>): Item {
        val registeredItem = Registry.register(Registries.ITEM, registryKey.value, item)

        return registeredItem
    }

    fun initialize() {
        metalChunk = register(Item(metalChunkItem), METAL_CHUNK_KEY)

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
            .register { group ->
                group.add(metalChunk)
            }

    }

}
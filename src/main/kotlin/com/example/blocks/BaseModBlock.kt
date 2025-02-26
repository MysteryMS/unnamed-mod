package com.example.blocks

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

interface BaseModBlock {
    val id: String
    val identifier: Identifier
        get() = Identifier.of("template-mod", id)
    val key: RegistryKey<Block>
        get() = RegistryKey.of(RegistryKeys.BLOCK, identifier)

    val settings: AbstractBlock.Settings
        get() = AbstractBlock.Settings.create().registryKey(key)

    var block: Block?
}
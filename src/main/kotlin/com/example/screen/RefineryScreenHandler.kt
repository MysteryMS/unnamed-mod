package com.example.screen

import com.example.blocks.refinery.RefineryEntity
import com.example.network.RefineryScreenUpdatePayload
import com.example.registries.ModScreenHandlers
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import kotlin.jvm.optionals.getOrNull


class RefineryScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory?,
    val entity: RefineryEntity,
    private var _payload: RefineryScreenUpdatePayload
) : ScreenHandler(ModScreenHandlers.refineryScreenHandlerType, syncId) {
    var currentRecipeOutputs: List<ItemStack>? = null

    var payload: RefineryScreenUpdatePayload
        get() = _payload
        set(value) = updatePayload(value)

    constructor(syncId: Int, playerInventory: PlayerInventory, payload: RefineryScreenUpdatePayload) : this(
        syncId,
        playerInventory,
        playerInventory.player.world.getBlockEntity(payload.blockPos) as RefineryEntity,
        payload
    )

    init {
        addPlayerHotbarSlots(playerInventory, 8, 142)
        addPlayerInventorySlots(playerInventory, 8, 84)
        val inv = this.entity.inventory
        addSlot(Slot(inv, 0, 16, 27))
        addSlot(object : Slot(inv, 1, 83, 52) {
            override fun canInsert(stack: ItemStack?): Boolean {
                return false
            }
        })
        updatePayload(_payload)
    }

    fun updatePayload(incomingPayload: RefineryScreenUpdatePayload) {
        _payload = incomingPayload

        currentRecipeOutputs = incomingPayload.selectedRecipeOutputs.getOrNull()
        // tried to send the recipe over the network, but this seems a little tricky. Im just sending the outputs
//        val world = this.entity.world
//        if (world != null && world is ServerWorld) {
//            currentRecipe = payload.selectedRecipeId.getOrNull()?.let {
//                world.recipeManager.get(it)?.parent?.value as RefineryRecipe
//            }
//        }
    }

    override fun quickMove(player: PlayerEntity, slotIndex: Int): ItemStack {
        val slot = getSlot(slotIndex)
        var newStack = ItemStack.EMPTY
        if (slot != null && slot.hasStack()) {
            newStack = slot.stack.copy()
            if (slotIndex in 0..35) {
                if (!insertItem(slot.stack, 36, this.slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!insertItem(slot.stack, 0, 36, false)) {
                return ItemStack.EMPTY
            }

            if (slot.stack.isEmpty)
                slot.stack = ItemStack.EMPTY else
                slot.markDirty()
        }

        return newStack
    }

    override fun onContentChanged(inventory: Inventory?) {
        this.syncState()
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return true
    }
}
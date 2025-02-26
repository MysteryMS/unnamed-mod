package com.example.screen

import com.example.blocks.entities.refinery.RefineryEntity
import com.example.network.BlockPosPayload
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot

class RefineryScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory?,
    val entity: RefineryEntity
) :
    ScreenHandler(ScreenHandlerTypes.refineryScreenHandlerType, syncId) {

    constructor(syncId: Int, playerInventory: PlayerInventory, payload: BlockPosPayload) : this(
        syncId,
        playerInventory,
        playerInventory.player.world.getBlockEntity(payload.pos) as RefineryEntity
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

    }

    fun hello() {
        println("hello")
        println(entity)
        entity.output = Items.COPPER_INGOT
        entity.markDirty()
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
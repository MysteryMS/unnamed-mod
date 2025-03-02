package com.example.blocks.refinery

import com.example.EntityTypes
import com.example.network.BlockPosPayload
import com.example.screen.RefineryScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.math.floor

class RefineryEntity(pos: BlockPos?, state: BlockState?) : BlockEntity(EntityTypes.REFINERY, pos, state),
    ExtendedScreenHandlerFactory<BlockPosPayload> {

    val inventory = object : SimpleInventory(2) {}
    var output: Item? = null
    var processed = 0

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler {
        return RefineryScreenHandler(syncId, playerInventory, this)
    }

    override fun getDisplayName(): Text {
        return Text.literal("Refinaria")
    }

    override fun getScreenOpeningData(p0: ServerPlayerEntity?): BlockPosPayload {
        return BlockPosPayload(this.pos)
    }

    companion object {
        @JvmStatic
        fun tick(world: World, blockPos: BlockPos, blockState: BlockState, entity: RefineryEntity) {
            if (world.isClient) return
            entity.processed++

            if (entity.inventory.getStack(0).isEmpty) {
                entity.output = null
                return
            }

            if (entity.processed >= 20 * 3) {
                entity.processed = 0
                val outputSlot = entity.inventory.getStack(1)

                if (outputSlot.count == 64) return
                if (entity.output == null) return
                if (entity.output != outputSlot.item && outputSlot.item != Items.AIR) return

                entity.inventory.getStack(0).count--

                val stack = entity.inventory.getStack(1)
                val toAdd = floor(64 * Outcomes.metalChunkPercentages[entity.output]!!).toInt()

                if (stack.isEmpty) {
                    val newStack = ItemStack(entity.output)
                    newStack.count = toAdd
                    entity.inventory.setStack(1, newStack)
                } else {
                    stack.count += toAdd
                }
            }
        }
    }
}
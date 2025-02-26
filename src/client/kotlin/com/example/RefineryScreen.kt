package com.example

import com.example.blocks.entities.refinery.Outcomes
import com.example.network.BlockPosPayload
import com.example.network.RefineryOutputChange
import com.example.screen.RefineryScreenHandler
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.RenderLayer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class RefineryScreen(handler: RefineryScreenHandler?, inventory: PlayerInventory?, title: Text?) :
    HandledScreen<RefineryScreenHandler>(handler, inventory, title) {

    private val texture = Identifier.of("template-mod", "textures/gui/container/refinery.png")
    private var showOres = false
    private val availableOres: MutableList<Item> = mutableListOf()
    private var selected: Int? = null

    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {
        context?.drawTexture(
            RenderLayer::getGuiTextured,
            texture,
            this.x, this.y,
            0F, 0F,
            177, 166,
            256, 256
        )

        if (showOres) {
            availableOres.forEachIndexed { i, identifier ->
                val xBase = x + 45 + (i * 6) + (i * 18)
                val yBase = y + 26
                val hOffset = if (i == selected) 176F else 194F

                context?.drawTexture(
                    RenderLayer::getGuiTextured,
                    texture,
                    xBase, this.y + 26,
                    hOffset, 0F,
                    18, 18,
                    256, 256
                )

                context?.drawItem(ItemStack(identifier), xBase + 1, yBase + 1)

                if (mouseX in xBase..xBase + 18 && mouseY in yBase..yBase + 18) {
                    context?.drawTooltip(
                        textRenderer,
                        Text.literal("50%"),
                        xBase, yBase + 18 + 10,
                    )
                }
            }

        }
    }

    override fun handledScreenTick() {
        val slot = handler.getSlot(36)
        if (slot.stack.isEmpty) {
            availableOres.clear()
            showOres = false
            selected = null
            return
        }

        try {
            val block = (slot.stack.item as BlockItem).block

            if (block == ModBlocks.metalChunk.block) {
                if (availableOres.isEmpty()) {
                    availableOres.addAll(Outcomes.metalChunk)
                }
                showOres = true
            } else {
                availableOres.clear()
                selected = null
                showOres = false
            }
        } catch (e: Exception) {
            println("oops! ${e.message}")
        }

    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (selected != null) return super.mouseClicked(mouseX, mouseY, button)

        availableOres.forEachIndexed { i, item ->
            val xBase = (x + 45 + (i * 6) + (i * 18)) * 1.0
            val yBase = y + 26 * 1.0

            if (mouseX in xBase..xBase + 18 && mouseY in yBase..yBase + 18) {
                println("click on item $i")
                selected = i
                ClientPlayNetworking.send(RefineryOutputChange(handler.entity.pos, i))
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

}
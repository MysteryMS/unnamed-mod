package com.example

import com.example.blocks.refinery.RefineryStatus
import com.example.network.RefineryOutputChange
import com.example.recipes.RefineryRecipe
import com.example.screen.RefineryScreenHandler
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.render.RenderLayer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Colors
import net.minecraft.util.Identifier
import java.util.*
import kotlin.jvm.optionals.getOrNull
import kotlin.math.floor
import kotlin.math.round

class RefineryScreen(handler: RefineryScreenHandler?, val inventory: PlayerInventory?, title: Text?) :
    HandledScreen<RefineryScreenHandler>(handler, inventory, title) {

    private val texture = Identifier.of("template-mod", "textures/gui/container/refinery.png")

    private val selectedItem: Item?
        get() = handler.payload.outputItemSelectedIdentifier.getOrNull()?.let {
            Registries.ITEM.get(it)
        }

    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {
        context?.drawTexture(
            RenderLayer::getGuiTextured,
            texture,
            this.x, this.y,
            0F, 0F,
            176, 166,
            256, 256
        )

        val progressBarWidth = 229 - 176
        val progressBarHeight = 65 - 45
        val progressPixels = floor(handler.payload.processedItemPercent * progressBarWidth).toInt()


        context?.drawTexture(
            RenderLayer::getGuiTextured,
            texture,
            x + 22, y + 45,
            176F, 19F,
            progressPixels, progressBarHeight,
            256, 256
        )

        if (handler.payload.status == RefineryStatus.BLOCKED) {
            context?.drawTexture(
                RenderLayer::getGuiTextured,
                texture,
                x + 64, y + 59,
                176F, 40F,
                7, 8,
                256, 256
            )
        }

        handler.currentRecipeOutputs?.forEachIndexed { i, item ->
            val isSelected = item.item == selectedItem
            val xBase = x + 45 + (i * 6) + (i * 18)
            val yBase = y + 26
            val hOffset = if (isSelected) 176F else 194F

            context?.drawTexture(
                RenderLayer::getGuiTextured,
                texture,
                xBase, this.y + 26,
                hOffset, 0F,
                18, 18,
                256, 256
            )

            context?.drawItem(item, xBase + 1, yBase + 1)
            context?.drawStackOverlay(MinecraftClient.getInstance().textRenderer, item, xBase + 1, yBase + 1)
        }
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        drawMouseoverTooltip(context, mouseX, mouseY);

        handler.currentRecipeOutputs?.forEachIndexed { i, item ->
            val xBase = (x + 45 + (i * 6) + (i * 18)) * 1
            val yBase = y + 26 * 1

            if (mouseX in xBase..xBase + 18 && mouseY in yBase..yBase + 18) {
                context?.drawItemTooltip(
                    this.textRenderer,
                    item,
                    mouseX, mouseY
                );
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
//        if (selected != null) return super.mouseClicked(mouseX, mouseY, button)

        // TODO: change to button widget
        handler.currentRecipeOutputs?.forEachIndexed { i, item ->
            val xBase = (x + 45 + (i * 6) + (i * 18)) * 1.0
            val yBase = y + 26 * 1.0

            if (mouseX in xBase..xBase + 18 && mouseY in yBase..yBase + 18) {
                inventory?.player?.playSound(SoundEvents.UI_BUTTON_CLICK.value())
                val selectedItemIdentifier = Registries.ITEM.getId(item.item)
                val selectedValue = if (item.item == selectedItem) null else selectedItemIdentifier

                ClientPlayNetworking.send(RefineryOutputChange(
                    handler.entity.pos,
                    Optional.ofNullable(selectedValue)
                ))
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

}
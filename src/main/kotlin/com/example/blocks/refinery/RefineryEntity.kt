package com.example.blocks.refinery

import com.example.blocks.entities.EntityTypes
import com.example.network.RefineryScreenUpdatePayload
import com.example.recipes.RefineryRecipe
import com.example.screen.RefineryScreenHandler
import com.example.utils.getIntNullable
import com.example.utils.getStringNullable
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.NetworkRecipeId
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.Optional
import kotlin.jvm.optionals.getOrNull
import kotlin.math.floor

class RefineryEntity(pos: BlockPos?, state: BlockState?) : BlockEntity(EntityTypes.REFINERY, pos, state),
    ExtendedScreenHandlerFactory<RefineryScreenUpdatePayload>
{
    val inventory = object : SimpleInventory(2) {}
    var output: Item? = null
    var processedTicks = 0
    var isBlocked = false

    val processedItemPercent: Float
        get() = processedTicks / TIME_TO_PROCESS_ITEM_TICKS.toFloat()

    val inputStack: ItemStack
        get() = inventory.getStack(0)

    val outputStack: ItemStack
        get() = inventory.getStack(1)

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler {
        return RefineryScreenHandler(syncId, playerInventory, this, createScreenPayload())
    }

    override fun getDisplayName(): Text {
        return Text.literal("Refinaria")
    }

    private fun createScreenPayload() = RefineryScreenUpdatePayload(
        blockPos = pos,
//        selectedRecipeIdentifier = Optional.ofNullable(getRecipe()),
        selectedRecipeOutputs = Optional.ofNullable(getRecipe()?.let { it.value?.outputs }),
        outputItemSelectedIdentifier = Optional.ofNullable(output?.let { Registries.ITEM.getId(output) }),
        processedItemPercent = processedItemPercent,
        isBlocked = isBlocked,
    )

    override fun getScreenOpeningData(player: ServerPlayerEntity?) = createScreenPayload()

    private fun updateScreenData() {
        markDirty()
        val players =
            PlayerLookup.tracking(this) // TODO: may not be the best way to get players. would be better to get players with the screen opened
        val payload = createScreenPayload()
        players.forEach {
            ServerPlayNetworking.send(it, payload)
        }
    }

    override fun writeNbt(nbt: NbtCompound, registries: RegistryWrapper.WrapperLookup) {
        Inventories.writeNbt(nbt, this.inventory.heldStacks, registries);
        println("wrote nbt")

        if (output != null) {
            nbt.putString("outputItemIdentifier", Registries.ITEM.getId(output).toString())
        } else {
            nbt.remove("outputItemIdentifier")
        }
        nbt.putInt("processedTicks", processedTicks)


        super.writeNbt(nbt, registries)
    }

    override fun readNbt(nbt: NbtCompound, registries: RegistryWrapper.WrapperLookup?) {
        super.readNbt(nbt, registries)
        Inventories.readNbt(nbt, this.inventory.heldStacks, registries)

        output = nbt.getStringNullable("outputItemIdentifier")
            ?.let { Registries.ITEM.get(Identifier.of(it)) }
        nbt.getIntNullable("processedTicks")?.let { processedTicks = it }
    }

    fun getRecipe(): RecipeEntry<RefineryRecipe>? {
        if (world is ServerWorld) {
            val recipeInput = SingleStackRecipeInput(inputStack)
            val recipe =
                (world as ServerWorld).recipeManager.getFirstMatch(RefineryRecipe.Type.instance, recipeInput, world)
                    .getOrNull()

            return recipe
        }
        return null
    }

    fun updateOutcomeItem(itemIdentifier: Identifier?) {
        output = itemIdentifier?.let { Registries.ITEM.get(it) }
        updateScreenData()
    }


    fun tick(world: World, blockPos: BlockPos, blockState: BlockState, entity: RefineryEntity) {
        // TODO: optimize this function. a lot of checks and network updates are being done every tick
        // TODO: maybe create a update function whenever the slots are changed or the process is finished
        if (world.isClient) return

        if (entity.inputStack.isEmpty) {
            // no item to process
            entity.isBlocked = false
            entity.output = null
            entity.updateScreenData()
            return
        }

        if (entity.output == null) {
            // no output selected
            entity.isBlocked = true
            entity.updateScreenData()
            return
        }

        val recipe = getRecipe()?.value
        if (recipe == null) {
            entity.isBlocked = true
            updateScreenData()
            return;
        }


        val recipeOutputStack = recipe.outputs.find {
            it.item == output
        } ?: return

        val toAdd = outputStack.count

        if (entity.outputStack.count + toAdd > 64) {
            // no space to process more
            entity.isBlocked = true
            entity.updateScreenData()
            return
        }

        if (entity.output != entity.outputStack.item && entity.outputStack.item != Items.AIR) {
            // cannot put item to output stack
            entity.isBlocked = true
            entity.updateScreenData()
            return
        }
        entity.isBlocked = false

        if (entity.processedTicks++ >= TIME_TO_PROCESS_ITEM_TICKS) {
            entity.processedTicks = 0

            entity.inputStack.count--

            if (entity.outputStack.isEmpty) {
                val newStack = recipeOutputStack.copy()
                entity.inventory.setStack(1, newStack)
            } else {
                entity.outputStack.count += toAdd
            }
        }
        entity.updateScreenData()
    }

    companion object {
        private const val TIME_TO_PROCESS_ITEM_TICKS = 20 * 3 // 3s
    }
}
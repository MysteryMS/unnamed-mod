package com.example.blocks.refinery

import com.example.registries.ModEntityTypes
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
import net.minecraft.recipe.RecipeEntry
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

class RefineryEntity(pos: BlockPos?, state: BlockState?) : BlockEntity(ModEntityTypes.REFINERY, pos, state),
    ExtendedScreenHandlerFactory<RefineryScreenUpdatePayload>
{
    private val TIME_TO_PROCESS_ITEM_TICKS = 20 * 3 // 3s

    val inventory = SimpleInventory(2)

    var status: RefineryStatus = RefineryStatus.IDLE
        private set
    var output: Item? = null
    var processedTicks = 0

    val processedItemPercent: Float
        get() = processedTicks / TIME_TO_PROCESS_ITEM_TICKS.toFloat()

    val inputStack: ItemStack
        get() = inventory.getStack(0)

    var outputStack: ItemStack
        get() = inventory.getStack(1)
        set(value) = inventory.setStack(1, value)

    init {
        inventory.addListener { updateState() }
    }

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
        status = status
    )

    override fun getScreenOpeningData(player: ServerPlayerEntity?) = createScreenPayload()

    private fun updateScreenData() {
        if (world?.isClient == true) return

        // TODO: optimize to only send packet if its different from last one
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
        updateState()
    }

    private fun updateState() {
        if (inputStack.isEmpty) {
            // no item to process
            status = RefineryStatus.IDLE
            output = null
            updateScreenData()
            return
        }

        if (output == null) {
            // no output selected
            status = RefineryStatus.BLOCKED
            updateScreenData()
            return
        }

        val recipe = getRecipe()?.value
        if (recipe == null) {
            // no recipe found for input stack
            status = RefineryStatus.BLOCKED
            updateScreenData()
            return;
        }
        val recipeOutputStack = getRecipe()?.value?.outputs?.find {
            it.item == output
        } ?: return

        val toAdd = recipeOutputStack.count

        if (outputStack.count + toAdd > 64) {
            // no space to process more
            status = RefineryStatus.BLOCKED
            updateScreenData()
            return
        }

        if (output != outputStack.item && outputStack.item != Items.AIR) {
            // cannot put item to output stack
            status = RefineryStatus.BLOCKED
            updateScreenData()
            return
        }
        status = RefineryStatus.PROCESSING

        updateScreenData()
    }

    private fun processItem() {
        val recipeOutputStack = getRecipe()?.value?.outputs?.find {
            it.item == output
        } ?: return

        inputStack.count--

        if (outputStack.isEmpty) {
            outputStack = recipeOutputStack.copy()
        } else {
            outputStack.count += recipeOutputStack.count
        }
        updateState()
    }

    fun tick(world: World, blockPos: BlockPos, blockState: BlockState, entity: RefineryEntity) {
        if (world.isClient) return

        if (status == RefineryStatus.PROCESSING) {
            if (entity.processedTicks++ >= TIME_TO_PROCESS_ITEM_TICKS) {
                processItem()
                entity.processedTicks = 0
            }
            updateState()
        }
    }
}
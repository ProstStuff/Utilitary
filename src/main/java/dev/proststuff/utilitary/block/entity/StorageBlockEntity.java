package dev.proststuff.utilitary.block.entity;

import dev.proststuff.utilitary.block.state.Illuminance;
import dev.proststuff.utilitary.utility.Storage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public abstract class StorageBlockEntity extends SyncedBlockEntity implements Storage {
    public StorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void dropAll() {
        if (world != null && world instanceof ServerWorld serverWorld) {
            for (ItemStack stack : getItems()) {
                serverWorld.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
            }
        }

        clear();
    }

    public void playerStoreItem(PlayerEntity player, ItemStack heldItem, int slot) {
        setStack(slot, heldItem.splitUnlessCreative(getItemStackLimit(), player));
    }

    public void playerTakeOrExchange(PlayerEntity player, ItemStack heldStack, int slot) {
        ItemStack taken = removeStack(slot);

        if (player.isCreative()) {
            if (!player.getInventory().contains(taken)) player.giveItemStack(taken);
        } else if (!player.giveItemStack(taken)) {
            player.dropItem(taken, false);
        }

        if (!heldStack.isEmpty()) {
            playerStoreItem(player, heldStack, slot);
        }
    }

    @Override
    public void markDirty() {
        if (world != null && !world.isClient && !world.getBlockState(pos).isAir()) {
            BlockState state = getCachedState();
            if (state.getBlock() instanceof Illuminance illuminance) {
                illuminance.forceIlluminanceUpdate(state, world, pos);
            }
            world.markDirty(pos);
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public void markRemoved() {
        dropAll();
        super.markRemoved();
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, getItems(), registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, getItems(), registryLookup);
    }
}
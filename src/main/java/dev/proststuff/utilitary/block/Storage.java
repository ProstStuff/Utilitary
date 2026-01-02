package dev.proststuff.utilitary.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public interface Storage extends SidedInventory {
    DefaultedList<ItemStack> getItems();

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack stack : getItems()) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    default int getMaxCountPerStack() {
        return 64;
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);

        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    @Override
    default ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(getItems(), slot, amount);
    }

    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    @Override
    default void clear() {
        getItems().clear();
    }

    @Override
    default int[] getAvailableSlots(Direction side) {
        int[] slots = new int[size()];
        for (int i = 0; i < slots.length; i++) slots[i] = i;
        return slots;
    }

    @Override
    default boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    default boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
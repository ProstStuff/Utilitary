package dev.proststuff.utilitary.utility;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * An implemented `Inventory` with a few extra helpers.
 */
public interface Storage extends SidedInventory {
    static Storage of(int size) {
        return new Storage() {
            private final DefaultedList<ItemStack> items = DefaultedList.ofSize(size, ItemStack.EMPTY);

            @Override
            public DefaultedList<ItemStack> getItems() {
                return items;
            }
        };
    }

    DefaultedList<ItemStack> getItems();

    default int getItemStackLimit() {
        return Item.DEFAULT_MAX_COUNT;
    }

    default ItemStack getFirst() {
        for (ItemStack item : getItems()) {
            if (!item.isEmpty()) return item;
        }

        return ItemStack.EMPTY;
    }

    default ItemStack getLast() {
        for (int i = getItems().size() - 1; i >= 0; i--) {
            ItemStack item = getStack(i);

            if (!item.isEmpty()) return item;
        }

        return ItemStack.EMPTY;
    }

    @Override
    default int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    @Override
    default boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        return true;
    }

    @Override
    default boolean canExtract(int slot, ItemStack stack, Direction side) {
        return true;
    }

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    default ItemStack removeStack(int slot, int count, boolean markDirty) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);

        if (!result.isOf(getItems().get(slot).getItem()) && markDirty) {
            markDirty();
        }

        return result;
    }

    @Override
    default ItemStack removeStack(int slot, int count) {
        return removeStack(slot, count, true);
    }

    default ItemStack removeStack(int slot, boolean markDirty) {
        ItemStack removed = Inventories.removeStack(getItems(), slot);

        if (!removed.isOf(getItems().get(slot).getItem()) && markDirty) {
            markDirty();
        }

        return removed;
    }

    @Override
    default ItemStack removeStack(int slot) {
        return removeStack(slot, true);
    }

    default void setStack(int slot, ItemStack stack, boolean markDirty) {
        getItems().set(slot, stack);

        int limit = Math.min(stack.getMaxCount(), getItemStackLimit());
        if (stack.getCount() > limit) {
            stack.setCount(limit);
        }

        if (markDirty) {
            markDirty();
        }
    }

    default void setStack(int slot, ItemStack stack) {
        setStack(slot, stack, true);
    }

    default void clear(boolean markDirty) {
        getItems().clear();

        if (markDirty) {
            markDirty();
        }
    }

    @Override
    default void clear() {
        clear(true);
    }

    @Override
    default void markDirty() {}

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
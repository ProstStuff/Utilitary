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
public interface IStorage extends SidedInventory {
    static IStorage of(int size) {
        return new IStorage() {
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

    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        markDirty();

        int limit = Math.min(stack.getMaxCount(), getItemStackLimit());
        if (stack.getCount() > limit) {
            stack.setCount(limit);
        }
    }

    @Override
    default void clear() {
        getItems().clear();
        markDirty();
    }

    @Override
    default void markDirty() {}

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
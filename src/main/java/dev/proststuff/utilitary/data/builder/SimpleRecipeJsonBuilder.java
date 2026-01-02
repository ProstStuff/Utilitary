package dev.proststuff.utilitary.data.builder;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class SimpleRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    protected final ItemStack output;
    protected final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    protected String group;

    public SimpleRecipeJsonBuilder(ItemStack result) {
        this.output = result;
    }

    public SimpleRecipeJsonBuilder(ItemConvertible result, int count) {
        this(new ItemStack(result, count));
    }

    public SimpleRecipeJsonBuilder(ItemConvertible result) {
        this(result, 1);
    }

    public SimpleRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        criteria.put(name, criterion);
        return this;
    }

    public SimpleRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public Item getOutputItem() {
        return output.getItem();
    }
}
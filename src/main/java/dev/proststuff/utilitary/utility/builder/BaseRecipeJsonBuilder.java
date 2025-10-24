package dev.proststuff.utilitary.utility.builder;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base recipe json builder from NeoForge's SimpleRecipeBuilder documentation
 */
public abstract class BaseRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    protected final ItemStack result;
    protected final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    protected String group;

    public BaseRecipeJsonBuilder(ItemStack result) {
        this.result = result;
    }

    public BaseRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public BaseRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public Item getResult() {
        return this.result.getItem();
    }
}
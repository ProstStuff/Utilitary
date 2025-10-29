package dev.proststuff.utilitary.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "deprecation", "unused"})
public abstract class UtilitaryTagProvider<T> extends FabricTagProvider<T> {
    public UtilitaryTagProvider(FabricDataOutput output, RegistryKey<? extends Registry<T>> registryKey, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registryKey, registriesFuture);
    }

    protected void addTagTo(TagKey<T> tag, TagKey<?>... addToTags) {
        for (TagKey<?> tagKey : addToTags) {
            if (tagKey.isOf(registryRef)) {
                getOrCreateTagBuilder((TagKey<T>) tagKey).forceAddTag(tag);
            }
        }
    }

    protected void addTagsInto(TagKey<T> tag, TagKey<?>... addIntoTags) {
        for (TagKey<?> tagKey : addIntoTags) {
            if (tagKey.isOf(registryRef)) {
                getOrCreateTagBuilder(tag).forceAddTag((TagKey<T>) tagKey);
            }
        }
    }

    public abstract static class BlockTagProvider extends UtilitaryTagProvider<Block> {
        public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.BLOCK, registriesFuture);
        }

        @Override
        protected RegistryKey<Block> reverseLookup(Block element) {
            return element.getRegistryEntry().registryKey();
        }
    }

    public abstract static class BlockEntityTypeTagProvider extends UtilitaryTagProvider<BlockEntityType<?>> {
        public BlockEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, RegistryKeys.BLOCK_ENTITY_TYPE, completableFuture);
        }

        @Override
        protected RegistryKey<BlockEntityType<?>> reverseLookup(BlockEntityType<?> element) {
            assert element.getRegistryEntry() != null;
            return element.getRegistryEntry().registryKey();
        }
    }

    public abstract static class ItemTagProvider extends UtilitaryTagProvider<Item> {
        @Nullable
        private final Function<TagKey<Block>, TagBuilder> blockTagBuilderProvider;

        public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable UtilitaryTagProvider.BlockTagProvider blockTagProvider) {
            super(output, RegistryKeys.ITEM, completableFuture);

            this.blockTagBuilderProvider = blockTagProvider == null ? null : blockTagProvider::getTagBuilder;
        }

        public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            this(output, completableFuture, null);
        }

        public void copy(TagKey<Block> blockTag, TagKey<Item> itemTag) {
            TagBuilder blockTagBuilder = Objects.requireNonNull(this.blockTagBuilderProvider, "Pass Block tag provider via constructor to use copy").apply(blockTag);
            TagBuilder itemTagBuilder = getTagBuilder(itemTag);
            blockTagBuilder.build().forEach(itemTagBuilder::add);
        }

        @Override
        protected RegistryKey<Item> reverseLookup(Item element) {
            return element.getRegistryEntry().registryKey();
        }
    }

    public abstract static class FluidTagProvider extends UtilitaryTagProvider<Fluid> {
        public FluidTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, RegistryKeys.FLUID, completableFuture);
        }

        @Override
        protected RegistryKey<Fluid> reverseLookup(Fluid element) {
            return element.getRegistryEntry().registryKey();
        }
    }

    public abstract static class EnchantmentTagProvider extends UtilitaryTagProvider<Enchantment> {
        public EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, RegistryKeys.ENCHANTMENT, completableFuture);
        }
    }

    public abstract static class EntityTypeTagProvider extends UtilitaryTagProvider<EntityType<?>> {
        public EntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, RegistryKeys.ENTITY_TYPE, completableFuture);
        }

        @Override
        protected RegistryKey<EntityType<?>> reverseLookup(EntityType<?> element) {
            return element.getRegistryEntry().registryKey();
        }
    }
}
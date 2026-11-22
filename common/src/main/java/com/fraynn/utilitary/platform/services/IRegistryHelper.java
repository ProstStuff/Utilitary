package com.fraynn.utilitary.platform.services;

import com.fraynn.utilitary.platform.RegistryPair;
import com.fraynn.utilitary.platform.ServiceUtil;
import com.fraynn.utilitary.platform.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IRegistryHelper {
    IRegistryHelper INSTANCE = ServiceUtil.load(IRegistryHelper.class);

    <T> RegistryEntry<T> register(Registry<? super T> registry, Identifier id, Supplier<T> value);

    default <I extends Item> RegistryEntry<I> registerItem(Identifier id, Function<Item.Properties, I> factory, Consumer<Item.Properties> propertyBuilder) {
        return register(BuiltInRegistries.ITEM, id, () -> {
            ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
            Item.Properties properties = new Item.Properties();
            propertyBuilder.accept(properties);
            properties.setId(key);

            return factory.apply(properties);
        });
    }

    default <B extends Block> RegistryEntry<B> registerBlock(Identifier id, Function<BlockBehaviour.Properties, B> factory, Consumer<BlockBehaviour.Properties> propertyBuilder) {
        return register(BuiltInRegistries.BLOCK, id, () -> {
            ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
            propertyBuilder.accept(properties);
            properties.setId(key);

            return factory.apply(properties);
        });
    }

    default <B extends Block, I extends BlockItem> RegistryPair<B, I> registerBlockWithItem(Identifier id, Function<BlockBehaviour.Properties, B> blockFactory, Consumer<BlockBehaviour.Properties> blockPropertyBuilder, BiFunction<B, Item.Properties, I> itemFactory, Consumer<Item.Properties> itemPropertyBuilder) {
        RegistryEntry<B> block = registerBlock(id, blockFactory, blockPropertyBuilder);
        RegistryEntry<I> item = registerItem(id, (itemProp) -> itemFactory.apply(block.get(), itemProp), itemPropertyBuilder);
        return new RegistryPair<>(block, item);
    }

    default <B extends Block> RegistryPair<B, BlockItem> registerBlockWithItem(Identifier id, Function<BlockBehaviour.Properties, B> blockFactory, Consumer<BlockBehaviour.Properties> blockPropertyBuilder) {
        return registerBlockWithItem(id, blockFactory, blockPropertyBuilder, BlockItem::new, Item.Properties::useBlockDescriptionPrefix);
    }
}
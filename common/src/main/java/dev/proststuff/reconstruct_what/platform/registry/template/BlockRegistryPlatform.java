package dev.proststuff.reconstruct_what.platform.registry.template;

import dev.proststuff.reconstruct_what.platform.registry.RegistryEntry;
import dev.proststuff.reconstruct_what.platform.registry.RegistryPlatform;
import dev.proststuff.reconstruct_what.platform.services.AbstractPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class BlockRegistryPlatform extends RegistryPlatform<Block> {
    public BlockRegistryPlatform(AbstractPlatform platform) {
        super(platform, BuiltInRegistries.BLOCK);
    }

    public <B extends Block> RegistryEntry<B> registerBlockWithItem(String name, Supplier<B> supplier, Item.Properties properties) {
        RegistryPlatform<Item> p = PLATFORM.getRegistryOrThrow(BuiltInRegistries.ITEM);

        RegistryEntry<B> b = register(name, supplier);
        p.register(name, () -> new BlockItem(b.get(), properties));
        return b;
    }

    public <B extends Block> RegistryEntry<B> registerBlockWithItem(String name, Supplier<B> supplier) {
        return registerBlockWithItem(name, supplier, new Item.Properties());
    }
}

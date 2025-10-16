package dev.proststuff.reconstruct_what.platform.registry.template;

import dev.proststuff.reconstruct_what.platform.registry.RegistryEntry;
import dev.proststuff.reconstruct_what.platform.registry.RegistryPlatform;
import dev.proststuff.reconstruct_what.platform.AbstractPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ItemRegistryPlatform extends RegistryPlatform<Item> {
    public ItemRegistryPlatform(AbstractPlatform platform) {
        super(platform, BuiltInRegistries.ITEM);
    }

    public RegistryEntry<Item> registerSimpleItem(String name, Item.Properties properties) {
        return register(name, () -> new Item(properties));
    }

    public RegistryEntry<Item> registerSimpleItem(String name) {
        return registerSimpleItem(name, new Item.Properties());
    }
}
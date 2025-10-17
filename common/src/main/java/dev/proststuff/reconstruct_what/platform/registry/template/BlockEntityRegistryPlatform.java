package dev.proststuff.reconstruct_what.platform.registry.template;

import dev.proststuff.reconstruct_what.platform.registry.RegistryPlatform;
import dev.proststuff.reconstruct_what.platform.services.AbstractPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityRegistryPlatform extends RegistryPlatform<BlockEntityType<?>> {
    public BlockEntityRegistryPlatform(AbstractPlatform platform) {
        super(platform, BuiltInRegistries.BLOCK_ENTITY_TYPE);
    }

}

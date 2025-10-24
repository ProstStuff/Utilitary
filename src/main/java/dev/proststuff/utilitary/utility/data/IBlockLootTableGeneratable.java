package dev.proststuff.utilitary.utility.data;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;

public interface IBlockLootTableGeneratable {
    default void generateLootTable(FabricBlockLootTableProvider lootTableProvider) {
        lootTableProvider.addDrop((Block) this);
    }
}

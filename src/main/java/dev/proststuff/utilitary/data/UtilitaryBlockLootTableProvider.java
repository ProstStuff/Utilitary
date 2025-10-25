package dev.proststuff.utilitary.data;

import dev.proststuff.utilitary.utility.data.IBlockLootTableGeneratable;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public abstract class UtilitaryBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected final FabricDataOutput dataOutput;

    protected UtilitaryBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
        this.dataOutput = dataOutput;
    }

    @Override
    public void generate() {
        for (Identifier id : Registries.BLOCK.getIds()) {
            if (!dataOutput.getModId().equals(id.getNamespace())) continue;

            Block b = Registries.BLOCK.get(id);

            if (b instanceof IBlockLootTableGeneratable blockLootTableGeneratable) {
                blockLootTableGeneratable.generateLootTable(this);
            }
        }
    }
}

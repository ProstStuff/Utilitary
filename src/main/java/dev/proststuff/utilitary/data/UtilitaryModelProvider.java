package dev.proststuff.utilitary.data;

import dev.proststuff.utilitary.utility.data.IModelGeneratable;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public abstract class UtilitaryModelProvider extends FabricModelProvider {
    protected FabricDataOutput output;

    public UtilitaryModelProvider(FabricDataOutput output) {
        super(output);
        this.output = output;
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (Identifier id : Registries.BLOCK.getIds()) {
            if (!output.getModId().equals(id.getNamespace())) continue;

            Block b = Registries.BLOCK.get(id);
            if (b instanceof IModelGeneratable modelGeneratable) {
                modelGeneratable.generateBlockStateModel(blockStateModelGenerator);
            }
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (Identifier id : Registries.ITEM.getIds()) {
            if (!output.getModId().equals(id.getNamespace())) continue;

            Item i = Registries.ITEM.get(id);
            if (i instanceof IModelGeneratable modelGeneratable) {
                if (!modelGeneratable.useBlockAsItemModel()) {
                    modelGeneratable.generateItemModel(itemModelGenerator);
                }
            }
        }
    }
}

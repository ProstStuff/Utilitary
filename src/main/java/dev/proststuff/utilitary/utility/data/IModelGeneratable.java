package dev.proststuff.utilitary.utility.data;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public interface IModelGeneratable {
    default void generateBlockStateModel(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll((Block) this);
        generateItemModelFromBlock(blockStateModelGenerator);
    }

    default void generateItemModel(ItemModelGenerator itemModelGenerator) {
        if (this instanceof ItemConvertible item) {
            itemModelGenerator.register(item.asItem(), Models.GENERATED);
        }
    }

    default void generateItemModelFromBlock(BlockStateModelGenerator blockStateModelGenerator) {
        if (this instanceof Block block && block.asItem() != null && useBlockAsItemModel()) {
            Identifier id = Registries.BLOCK.getId(block);
            blockStateModelGenerator.registerParentedItemModel(block.asItem(), id);
        }
    }

    default boolean useBlockAsItemModel() {return false;}
}
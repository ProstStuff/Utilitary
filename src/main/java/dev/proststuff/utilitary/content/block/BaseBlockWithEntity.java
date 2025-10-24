package dev.proststuff.utilitary.content.block;

import dev.proststuff.utilitary.content.block.entity.BaseStorageBlockEntity;
import dev.proststuff.utilitary.utility.RegistryUtil;
import dev.proststuff.utilitary.utility.StringUtil;
import dev.proststuff.utilitary.utility.data.IBlockLootTableGeneratable;
import dev.proststuff.utilitary.utility.data.IModelGeneratable;
import dev.proststuff.utilitary.utility.data.ILanguageGeneratable;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseBlockWithEntity extends BlockWithEntity implements ILanguageGeneratable, IBlockLootTableGeneratable, IModelGeneratable {
    public BaseBlockWithEntity(Settings settings) {
        super(settings);
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!newState.equals(state)) {
            if (world.getBlockEntity(pos) instanceof BaseStorageBlockEntity storageBlockEntity) {
                storageBlockEntity.dropAll();
            }
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public String getTranslation(String lang) {
        Identifier identifier = RegistryUtil.getIdentifierOrThrow(Registries.BLOCK, this);
        return StringUtil.format(identifier.getPath());
    }
}
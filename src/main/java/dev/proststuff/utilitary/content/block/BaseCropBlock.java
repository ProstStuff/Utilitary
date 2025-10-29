package dev.proststuff.utilitary.content.block;

import dev.proststuff.utilitary.utility.RegistryUtil;
import dev.proststuff.utilitary.utility.StringUtil;
import dev.proststuff.utilitary.utility.data.IBlockLootTableGeneratable;
import dev.proststuff.utilitary.utility.data.ILanguageGeneratable;
import dev.proststuff.utilitary.utility.data.IModelGeneratable;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.CropBlock;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public abstract class BaseCropBlock extends CropBlock implements ILanguageGeneratable, IBlockLootTableGeneratable, IModelGeneratable {
    public BaseCropBlock(Settings settings) {
        super(settings);
    }

    public abstract void generateLootTable(FabricBlockLootTableProvider lootTableProvider);

    @Override
    public boolean useBlockAsItemModel() {
        return true;
    }

    @Override
    public String getTranslation(String lang) {
        Identifier identifier = RegistryUtil.getIdentifierOrThrow(Registries.BLOCK, this);
        return StringUtil.format(identifier.getPath());
    }
}
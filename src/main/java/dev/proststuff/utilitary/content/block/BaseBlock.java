package dev.proststuff.utilitary.content.block;

import dev.proststuff.utilitary.utility.RegistryUtil;
import dev.proststuff.utilitary.utility.StringUtil;
import dev.proststuff.utilitary.utility.data.IBlockLootTableGeneratable;
import dev.proststuff.utilitary.utility.data.IModelGeneratable;
import dev.proststuff.utilitary.utility.data.ILanguageGeneratable;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public abstract class BaseBlock extends Block implements ILanguageGeneratable, IBlockLootTableGeneratable, IModelGeneratable {
    public BaseBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void generateLootTable(FabricBlockLootTableProvider lootTableProvider) {
        lootTableProvider.addDrop(this);
    }

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

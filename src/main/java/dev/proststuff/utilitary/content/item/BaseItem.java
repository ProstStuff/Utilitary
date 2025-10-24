package dev.proststuff.utilitary.content.item;

import dev.proststuff.utilitary.utility.RegistryUtil;
import dev.proststuff.utilitary.utility.StringUtil;
import dev.proststuff.utilitary.utility.data.IModelGeneratable;
import dev.proststuff.utilitary.utility.data.ILanguageGeneratable;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class BaseItem extends Item implements IModelGeneratable, ILanguageGeneratable {
    public BaseItem(Settings settings) {
        super(settings);
    }

    @Override
    public String getTranslation(String lang) {
        Identifier identifier = RegistryUtil.getIdentifierOrThrow(Registries.ITEM, this);
        return StringUtil.format(identifier.getPath());
    }
}
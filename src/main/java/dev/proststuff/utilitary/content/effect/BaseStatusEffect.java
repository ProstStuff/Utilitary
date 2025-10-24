package dev.proststuff.utilitary.content.effect;

import dev.proststuff.utilitary.utility.RegistryUtil;
import dev.proststuff.utilitary.utility.StringUtil;
import dev.proststuff.utilitary.utility.data.ILanguageGeneratable;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class BaseStatusEffect extends StatusEffect implements ILanguageGeneratable {
    public BaseStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public String getTranslation(String lang) {
        Identifier identifier = RegistryUtil.getIdentifierOrThrow(Registries.STATUS_EFFECT, this);
        return StringUtil.format(identifier.getPath());
    }
}

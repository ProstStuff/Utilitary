package dev.proststuff.utilitary.mixin;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FabricLanguageProvider.class)
public interface ILanguageCodeAccessor {
    @Accessor(value = "languageCode")
    String utilitary$getLanguageCode();
}

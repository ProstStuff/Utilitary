package dev.proststuff.utilitary.data;

import dev.proststuff.utilitary.utility.StringUtil;
import dev.proststuff.utilitary.utility.data.ILanguageGeneratable;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public abstract class UtilitaryLanguageProvider extends FabricLanguageProvider {
    protected final String languageCode;

    protected UtilitaryLanguageProvider(FabricDataOutput dataOutput, String languageCode, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, languageCode, registryLookup);
        this.languageCode = languageCode;
    }

    protected UtilitaryLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
        this.languageCode = "en_us";
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        iterateAndTranslate(translationBuilder, Registries.BLOCK);
        iterateAndTranslate(translationBuilder, Registries.ITEM);
        iterateAndTranslate(translationBuilder, Registries.STATUS_EFFECT);
        iterateAndTranslate(translationBuilder, Registries.ENTITY_TYPE);
    }

    protected <T> void iterateAndTranslate(TranslationBuilder translationBuilder, Registry<T> registry) {
        for (Identifier id : registry.getIds()) {
            if (!dataOutput.getModId().equals(id.getNamespace())) continue;

            T t = registry.get(id);
            if (t instanceof ILanguageGeneratable languageGeneratable) {
                String key = languageGeneratable.getTranslationKey().isEmpty() ? id.toTranslationKey() : languageGeneratable.getTranslationKey();
                String translation = languageGeneratable.getTranslation(languageCode).isEmpty() ? StringUtil.format(id.getNamespace()) : languageGeneratable.getTranslation(languageCode);
                translationBuilder.add(key, translation);
            }
        }
    }
}
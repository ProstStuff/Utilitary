package dev.proststuff.utilitary.utility;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

//TODO: Make parameters accept TagKey instead of the actual tag builder
public class TagProviderUtil {
    @SafeVarargs
    public static <T> void addTagsTo(FabricTagProvider<T>.FabricTagBuilder tagBuilder, TagKey<T>... tagKeys) {
        for (TagKey<T> tagKey : tagKeys) {
            tagBuilder.addTag(tagKey);
        }
    }

    public static <T> void addOptionalTagsTo(FabricTagProvider<T>.FabricTagBuilder tagBuilder, Identifier... tagKeys) {
        for (Identifier tagKey : tagKeys) {
            tagBuilder.addOptionalTag(tagKey);
        }
    }

    @SafeVarargs
    public static <T> void addOptionalTagsTo(FabricTagProvider<T>.FabricTagBuilder tagBuilder, TagKey<T>... tagKeys) {
        for (TagKey<T> tagKey : tagKeys) {
            tagBuilder.addOptionalTag(tagKey);
        }
    }

    @SafeVarargs
    public static <T> void addObjectsTo(FabricTagProvider<T>.FabricTagBuilder tagBuilder, T... objects) {
        for (T object : objects) {
            tagBuilder.add(object);
        }
    }

    public static <T> void addOptionalObjectsTo(FabricTagProvider<T>.FabricTagBuilder tagBuilder, Identifier... objects) {
        for (Identifier object : objects) {
            tagBuilder.addOptional(object);
        }
    }

    @SafeVarargs
    public static <T> void addOptionalObjectsTo(FabricTagProvider<T>.FabricTagBuilder tagBuilder, RegistryKey<? extends T>... objects) {
        for (RegistryKey<? extends T> object : objects) {
            tagBuilder.addOptional(object);
        }
    }
}

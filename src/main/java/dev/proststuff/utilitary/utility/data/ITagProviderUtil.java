package dev.proststuff.utilitary.utility.data;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public interface ITagProviderUtil<T> {
    Function<TagKey<T>, FabricTagProvider<T>.FabricTagBuilder> tagBuilder();
    RegistryKey<? extends Registry<T>> getRegistry();

    default void addTagsTo(TagKey<T> tag, TagKey<?>... tagKeys) {
        FabricTagProvider<T>.FabricTagBuilder tagBuilder = tagBuilder().apply(tag);

        for (TagKey<?> tagKey : tagKeys) {
            if (tagKey.isOf(getRegistry())) {
                tagBuilder.addTag((TagKey<T>) tagKey);
            }
        }
    }

    default void addOptionalTagsTo(TagKey<T> tag, Identifier... tagKeys) {
        FabricTagProvider<T>.FabricTagBuilder tagBuilder = tagBuilder().apply(tag);

        for (Identifier tagKey : tagKeys) {
            tagBuilder.addOptionalTag(tagKey);
        }
    }

    default void addOptionalTagsTo(TagKey<T> tag, TagKey<?>... tagKeys) {
        FabricTagProvider<T>.FabricTagBuilder tagBuilder = tagBuilder().apply(tag);

        for (TagKey<?> tagKey : tagKeys) {
            if (tagKey.isOf(getRegistry())) {
                tagBuilder.addOptionalTag((TagKey<T>) tagKey);
            }
        }
    }

    default void addObjectsTo(TagKey<T> tag, T... objects) {
        FabricTagProvider<T>.FabricTagBuilder tagBuilder = tagBuilder().apply(tag);

        for (T object : objects) {
            tagBuilder.add(object);
        }
    }

    default void addOptionalObjectsTo(TagKey<T> tag, Identifier... objects) {
        FabricTagProvider<T>.FabricTagBuilder tagBuilder = tagBuilder().apply(tag);

        for (Identifier object : objects) {
            tagBuilder.addOptional(object);
        }
    }

    default void addOptionalObjectsTo(TagKey<T> tag, RegistryKey<? extends T>... objects) {
        FabricTagProvider<T>.FabricTagBuilder tagBuilder = tagBuilder().apply(tag);

        for (RegistryKey<? extends T> object : objects) {
            tagBuilder.addOptional(object);
        }
    }
}

package dev.proststuff.utilitary.utility.data;

import net.minecraft.registry.tag.TagKey;

public interface CommonModTagKey<T> {
    TagKey<T> cTag();
    TagKey<T> modTag();
}
package dev.proststuff.utilitary.utility.data;

import net.minecraft.registry.tag.TagKey;

public interface ICModTagKey<T> {
    TagKey<T> getCTag();
    TagKey<T> getModTag();
}
package dev.proststuff.utilitary.utility;

import net.minecraft.nbt.NbtCompound;

public class NbtCompoundUtil {
    public static NbtCompound getCompound(NbtCompound nbtCompound, String key) {
        if (!nbtCompound.contains(key)) {
            nbtCompound.put(key, new NbtCompound());
        }

        return nbtCompound.getCompound(key);
    }
}

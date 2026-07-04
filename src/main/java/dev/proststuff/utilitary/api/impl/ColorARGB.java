package dev.proststuff.utilitary.api.impl;

import net.minecraft.util.ARGB;

public interface ColorARGB extends ColorRGB {
    int alpha();

    void setAlpha(int alpha);

    default void set(int alpha, int red, int green, int blue) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }

    default int asInt() {
        return ARGB.color(alpha(), red(), green(), blue());
    }

    default String asString() {
        return String.format("%08X", asInt());
    }
}

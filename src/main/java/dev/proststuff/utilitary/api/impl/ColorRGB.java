package dev.proststuff.utilitary.api.impl;

import net.minecraft.util.ARGB;

public interface ColorRGB {
    int red();
    int green();
    int blue();

    void setRed(int red);
    void setGreen(int green);
    void setBlue(int blue);

    default void set(int red, int green, int blue) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }

    default int asInt() {
        return ARGB.color(red(), green(), blue());
    }

    default String asString() {
        return String.format("%06X", asInt());
    }
}

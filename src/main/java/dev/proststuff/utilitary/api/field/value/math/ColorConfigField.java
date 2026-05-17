package dev.proststuff.utilitary.api.field.value.math;

import dev.proststuff.utilitary.api.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.field.ConfigField;
import dev.proststuff.utilitary.api.utility.Color;
import net.minecraft.util.ARGB;

public class ColorConfigField extends ConfigField<Color> {
    public ColorConfigField(String name, int value) {
        super(name, new Color(value), ConfigCodecs.COLOR);
    }

    public int getColor() {
        return get().get();
    }

    public int get(float alpha, float brightness) {
        return ARGB.setBrightness(ARGB.color(alpha, get().get()), brightness);
    }

    public int get(float alpha) {
        return withAlpha(alpha);
    }

    public int get(int alpha) {
        return get(alpha / 255.0F);
    }

    public int withAlpha(float alpha) {
        return ARGB.color(alpha, get().get());
    }

    public int withBrightness(float brightness) {
        return ARGB.setBrightness(get().get(), brightness);
    }

    public int getAlpha() {
        return ARGB.alpha(get().get());
    }

    public int getRed() {
        return ARGB.red(get().get());
    }

    public int getGreen() {
        return ARGB.green(get().get());
    }

    public int getBlue() {
        return ARGB.blue(get().get());
    }

    public int set(int color) {
        return get().set(color);
    }

    public int set(float a, float r, float g, float b) {
        return set(ARGB.colorFromFloat(a, r, g, b));
    }

    public int set(float r, float g, float b) {
        return set(1.0F, r, g, b);
    }

    public int set(int r, int g, int b) {
        return set(r / 255.0F, g / 255.0F, b / 255.0F);
    }

    public int set(int a, int r, int g, int b) {
        return set(a / 255.0F, r / 255.0F, g / 255.0F, b / 255.0F);
    }

    public int setAlpha(float alpha) {
        return get().setAlpha(alpha);
    }

    public int setAlpha(int alpha) {
        return get().setAlpha(alpha);
    }

    public int setRed(int r) {
        return setRed(r);
    }

    public int setRed(float r) {
        return setRed(r * 255);
    }

    public int setGreen(int g) {
        return setGreen(g);
    }

    public int setGreen(float g) {
        return setGreen(g * 255);
    }

    public int setBlue(int b) {
       return setBlue(b);
    }

    public int setBlue(float b) {
        return setBlue(b * 255);
    }
}
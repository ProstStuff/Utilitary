package dev.proststuff.utilitary.utility;

import net.minecraft.util.ARGB;

public class Color {
    protected int hex;

    public Color(int hex) {
        this.hex = hex;
    }

    public Color(float a, float r, float g, float b) {
        this(ARGB.colorFromFloat(a, r, g, b));
    }

    public Color(float r, float g, float b) {
        this(1.0F, r, g, b);
    }

    public Color(int r, int g, int b) {
        this(r / 255.0F, g / 255.0F, b / 255.0F);
    }

    public Color(int a, int r, int g, int b) {
        this(a / 255.0F, r / 255.0F, g / 255.0F, b / 255.0F);
    }

    public int get() {
        return hex;
    }

    public int get(float alpha, float brightness) {
        return ARGB.setBrightness(ARGB.color(alpha, get()), brightness);
    }

    public int get(float alpha) {
        return withAlpha(alpha);
    }

    public int get(int alpha) {
        return get(alpha / 255.0F);
    }

    public int withAlpha(float alpha) {
        return ARGB.color(alpha, get());
    }

    public int withBrightness(float brightness) {
        return ARGB.setBrightness(get(), brightness);
    }

    public int getAlpha() {
        return ARGB.alpha(get());
    }

    public int getRed() {
        return ARGB.red(get());
    }

    public int getGreen() {
        return ARGB.green(get());
    }

    public int getBlue() {
        return ARGB.blue(get());
    }

    public int set(int color) {
        this.hex = color;
        return this.hex;
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
        this.hex = ARGB.color(alpha, hex);
        return this.hex;
    }

    public int setAlpha(int alpha) {
        this.hex = ARGB.color(alpha, hex);
        return this.hex;
    }

    public int setRed(int r) {
        this.hex = ARGB.color(getAlpha(),r, getGreen(), getBlue());
        return this.hex;
    }

    public int setRed(float r) {
        return setRed(r * 255);
    }

    public int setGreen(int g) {
        this.hex = ARGB.color(getAlpha(), getRed(), g, getBlue());
        return this.hex;
    }

    public int setGreen(float g) {
        return setGreen(g * 255);
    }

    public int setBlue(int b) {
        this.hex = ARGB.color(getAlpha(), getRed(), b, getBlue());
        return this.hex;
    }

    public int setBlue(float b) {
        return setBlue(b * 255);
    }

    @Override
    public String toString() {
        return String.format("%08X", get());
    }
}
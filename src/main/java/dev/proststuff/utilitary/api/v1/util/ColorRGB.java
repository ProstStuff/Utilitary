package dev.proststuff.utilitary.api.v1.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Range;

import java.util.HexFormat;

public class ColorRGB {
    public static final Codec<ColorRGB> CODEC = Codec.STRING.comapFlatMap(ColorRGB::read, ColorRGB::toString);
    public static final StreamCodec<ByteBuf, ColorRGB> STREAM_CODEC = ByteBufCodecs.INT.map(ColorRGB::new, ColorRGB::get);

    protected int red;
    protected int green;
    protected int blue;

    protected boolean immutable = false;

    public ColorRGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public ColorRGB(float red, float green, float blue) {
        this(floatToInt(red), floatToInt(green), floatToInt(blue));
    }

    public ColorRGB(int color) {
        this(ARGB.red(color), ARGB.green(color), ARGB.blue(color));
    }

    public ColorRGB(String hex) {
        this(Integer.parseInt(hex, 16));
    }

    @SuppressWarnings("unchecked")
    public <T extends ColorRGB> T setImmutable() {
        this.immutable = true;
        return (T) this;
    }

    public boolean isImmutable() {
        return immutable;
    }

    public ColorRGB copy() {
        return new ColorRGB(red(), green(), blue());
    }

    public boolean hasAlpha() {
        return false;
    }

    public ColorARGB withAlpha(int alpha) {
        if (this instanceof ColorARGB colorWithAlpha && colorWithAlpha.alpha() == alpha && !isImmutable()) return (ColorARGB) this;
        return new ColorARGB(alpha, red(), green(), blue());
    }

    public ColorARGB withAlpha() {
        return withAlpha(255);
    }

    public ColorARGB withAlpha(float alpha) {
        return withAlpha(floatToInt(alpha));
    }

    public int get() {
        return ARGB.color(red(), green(), blue());
    }

    public int get(int alpha) {
        return ARGB.color(alpha, red(), green(), blue());
    }

    public int get(float alpha) {
        return ARGB.color(alpha, get());
    }

    public int red() {
        return red;
    }

    public float redFloat() {
        return red / 255.0F;
    }

    public int green() {
        return green;
    }

    public float greenFloat() {
        return green / 255.0F;
    }

    public int blue() {
        return blue;
    }

    public float blueFloat() {
        return blue / 255.0F;
    }

    public void setRed(int red) {
        if (isImmutable()) throw new IllegalStateException("ColorRGB is set as immutable");
        this.red = red;
    }

    public void setRed(float red) {
        setRed(floatToInt(red));
    }

    public void setGreen(int green) {
        if (isImmutable()) throw new IllegalStateException("ColorRGB is set as immutable");
        this.green = green;
    }

    public void setGreen(float green) {
        setGreen(floatToInt(green));
    }

    public void setBlue(int blue) {
        if (isImmutable()) throw new IllegalStateException("ColorRGB is set as immutable");
        this.blue = blue;
    }

    public void setBlue(float blue) {
        setBlue(floatToInt(blue));
    }

    public void set(int red, int green, int blue) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }

    public void set(float red, float green, float blue) {
        set(floatToInt(red), floatToInt(green), floatToInt(blue));
    }

    public void set(int color) {
        set(ARGB.red(color), ARGB.green(color), ARGB.blue(color));
    }

    @Override
    public String toString() {
        return HexFormat.of().toHexDigits(get(), 6);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof ColorRGB colorRGB && (colorRGB.get() == this.get() || colorRGB.toString().equals(this.toString()))) return true;
        return super.equals(obj);
    }

    public static DataResult<ColorRGB> read(String input) {
        input = input.strip();
        input = input.substring(Math.max(0, input.length() - 6));
        if (input.length() <= 6) input = String.format("%6s", input).replace(' ', 'F');
        if (ColorARGB.isARGB(input)) return DataResult.success(new ColorARGB(input));
        return DataResult.success(new ColorRGB(input));
    }

    public static @Range(from = 0, to = 255) int floatToInt(float f) {
        return Mth.floor(f * 255.0F);
    }
}
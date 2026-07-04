package dev.proststuff.utilitary.api.utility;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.proststuff.utilitary.api.impl.ColorRGB;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;

@SuppressWarnings("unused")
public class Color implements ColorRGB {
    public static final Codec<Color> CODEC = Codec.STRING.comapFlatMap(Color::read, Color::toString);
    public static final StreamCodec<ByteBuf, Color> STREAM_CODEC = ByteBufCodecs.INT.map(Color::new, Color::asInt);

    protected int red;
    protected int green;
    protected int blue;
    protected int color;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        refresh();
    }

    public Color(int color) {
        this(ARGB.red(color), ARGB.green(color), ARGB.blue(color));
    }

    public Color(String hex) {
        this(Integer.parseInt(hex, 16));
    }

    public ColorWithAlpha withAlpha(int alpha) {
        if (this instanceof ColorWithAlpha) return (ColorWithAlpha) this;
        return new ColorWithAlpha(alpha, this.red, this.green, this.blue);
    }

    public ColorWithAlpha withAlpha() {
        return withAlpha(255);
    }

    public int get() {
        return color;
    }

    public void refresh() {
        this.color = asInt();
    }

    @Override
    public int red() {
        return red;
    }

    @Override
    public int green() {
        return green;
    }

    @Override
    public int blue() {
        return blue;
    }

    @Override
    public void setRed(int red) {
        this.red = red;
        refresh();
    }

    @Override
    public void setGreen(int green) {
        this.green = green;
        refresh();
    }

    @Override
    public void setBlue(int blue) {
        this.blue = blue;
        refresh();
    }

    public static DataResult<Color> read(final String input) {
        return DataResult.success(new Color(input));
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof ColorRGB colorRGB && (colorRGB.asInt() == this.asInt() || colorRGB.asString().equals(this.asString()))) return true;
        return super.equals(obj);
    }
}
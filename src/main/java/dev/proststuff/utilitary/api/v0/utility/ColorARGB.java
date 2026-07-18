package dev.proststuff.utilitary.api.v0.utility;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;

import java.util.HexFormat;

public class ColorARGB extends ColorRGB {
    public static final Codec<ColorARGB> CODEC = Codec.STRING.comapFlatMap(ColorARGB::readAlpha, ColorARGB::toString);
    public static final StreamCodec<ByteBuf, ColorARGB> STREAM_CODEC = ByteBufCodecs.INT.map(ColorARGB::new, ColorARGB::get);

    protected int alpha;

    public ColorARGB(int alpha, int red, int green, int blue) {
        super(red, green, blue);
        this.alpha = alpha;
    }

    public ColorARGB(float alpha, float red, float green, float blue) {
        this(floatToInt(alpha * 255), floatToInt(red * 255), floatToInt(green * 255), floatToInt(blue * 255));
    }

    public ColorARGB(int color) {
        this(ARGB.alpha(color), ARGB.red(color), ARGB.green(color), ARGB.blue(color));
    }

    public ColorARGB(String hex) {
        this(Integer.parseUnsignedInt(hex, 16));
    }

    @Override
    public ColorARGB copy() {
        return new ColorARGB(alpha, red, green, blue);
    }

    public int getRGB() {
        return super.get();
    }

    public int alpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setAlpha(float alpha) {
        setAlpha(floatToInt(alpha));
    }

    @Override
    public boolean hasAlpha() {
        return true;
    }

    @Override
    public int get() {
        return ARGB.color(alpha(), red(), green(), blue());
    }

    @Override
    public String toString() {
        return HexFormat.of().toHexDigits(get(), 8);
    }

    public static DataResult<ColorARGB> readAlpha(String input) {
        input = input.strip();
        input = input.substring(Math.max(0, input.length() - 8));
        if (input.length() <= 8) input = String.format("%8s", input).replace(' ', 'F');
        return DataResult.success(new ColorARGB(input));
    }

    public static boolean isARGB(String hex) {
        hex = hex.strip();

        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        return hex.length() == 8;
    }
}
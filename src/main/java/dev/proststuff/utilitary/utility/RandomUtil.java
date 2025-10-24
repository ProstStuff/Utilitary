package dev.proststuff.utilitary.utility;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.util.math.random.Random;

public class RandomUtil {
    public static Random LOCAL_RANDOM = createBaseRandom();

    private static Random createBaseRandom() {
        long base = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        long mixed = base ^ System.nanoTime();
        return Random.create(mixed);
    }

    public static Random createRandom() {
        long base = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        long mixed = randomLong(LOCAL_RANDOM, 0, base);
        return Random.create(mixed);
    }

    public static float around(Random random, float base, float range) {
        return base + (random.nextFloat() - 0.5f) * range;
    }

    public static float pitch(Random random, float range) {
        return around(random, 1.0f, range);
    }

    public static float offset(Random random, float max) {
        return (random.nextFloat() - 0.5f) * 2f * max;
    }

    public static boolean chance(Random random, float probability) {
        return random.nextFloat() < probability;
    }

    public static boolean weight(Random random, int weight, int maxWeight) {
        return random.nextInt(maxWeight) < weight;
    }

    public static int sign(Random random) {
        return random.nextBoolean() ? 1 : -1;
    }

    public static <T> T oneOf(Random random, T[] array) {
        return array[random.nextInt(array.length)];
    }

    public static long randomLong(Random random, long min, long max) {
        if (min >= max)
            throw new IllegalArgumentException("max must be greater than min");

        long bound = max - min;
        long bits, val;
        do {
            bits = random.nextLong() >>> 1;
            val = bits % bound;
        } while (bits - val + (bound - 1) < 0L);

        return min + val;
    }
}
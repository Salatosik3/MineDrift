package io.github.salatosik3.minedrift.misc;

public final class FloatingPointUtils {
    private FloatingPointUtils() {}

    public static float cutLowest(float number, float minimum) {
        if (number < minimum) {
            return 0.0f;
        }
        return number;
    }

    public static float cutLowest(float number, int digits) {
        assert digits > 0;
        return cutLowest(number, 1.0f / (float) Math.pow(10, digits));
    }
}

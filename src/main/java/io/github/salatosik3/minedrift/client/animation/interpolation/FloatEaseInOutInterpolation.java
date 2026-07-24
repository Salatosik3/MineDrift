package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.animation.Clock;

public class FloatEaseInOutInterpolation extends AbstractInterpolation<Float> {
    public FloatEaseInOutInterpolation(Float min, Float max, long duration) {
        super(min, max, duration);
    }

    @Override
    protected Float compute(Float a, Float b, double t) {
        return (float) (a + (b - a) * t * t * (3 - 2 * t));
    }
}

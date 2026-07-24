package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.animation.Clock;

public class FloatLinearInterpolation extends AbstractInterpolation<Float> {
    public FloatLinearInterpolation(Float min, Float max, long duration) {
        super(min, max, duration);
    }

    @Override
    protected Float compute(Float a, Float b, double t) {
        return a + (b - a) * (float) t;
    }
}

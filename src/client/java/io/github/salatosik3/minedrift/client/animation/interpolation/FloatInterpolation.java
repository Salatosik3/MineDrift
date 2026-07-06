package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.animation.Clock;

public class FloatInterpolation extends AbstractInterpolation<Float> {
    public FloatInterpolation(InterpolationType type, Clock clock, Float min, Float max, long duration) {
        super(type, clock, min, max, duration);
    }

    public FloatInterpolation(InterpolationType type, Float min, Float max, long duration) {
        super(type, min, max, duration);
    }

    @Override
    protected Float compute(Float a, Float b, double t) {
        return super.type.getFunc().calculate(a, b, t).floatValue();
    }
}

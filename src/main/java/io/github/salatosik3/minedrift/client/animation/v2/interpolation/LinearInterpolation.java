package io.github.salatosik3.minedrift.client.animation.v2.interpolation;

public class LinearInterpolation extends AbstractInterpolation {
    public LinearInterpolation(long duration) {
        super(duration);
    }

    @Override
    protected float compute(float a, float b, float t) {
        return a + (b - a) * t;
    }
}

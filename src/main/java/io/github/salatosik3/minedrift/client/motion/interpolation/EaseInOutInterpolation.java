package io.github.salatosik3.minedrift.client.motion.interpolation;

public class EaseInOutInterpolation extends AbstractInterpolation {
    public EaseInOutInterpolation(long duration) {
        super(duration);
    }

    @Override
    protected float compute(float a, float b, float t) {
        return (a + (b - a) * t * t * (3 - 2 * t));
    }
}

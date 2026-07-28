package io.github.salatosik3.minedrift.client.motion.interpolation.mixed;

import io.github.salatosik3.minedrift.client.motion.interpolation.Interpolation;
import io.github.salatosik3.minedrift.client.motion.interpolation.LinearInterpolation;

public class Vec2LinearInterpolation extends AbstractVec2Interpolation {
    public Vec2LinearInterpolation(long duration) {
        super(duration);
    }

    @Override
    protected Interpolation getInterpolation(long duration) {
        return new LinearInterpolation(duration);
    }
}

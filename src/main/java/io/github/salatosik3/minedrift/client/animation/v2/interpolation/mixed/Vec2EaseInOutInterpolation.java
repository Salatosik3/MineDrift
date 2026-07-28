package io.github.salatosik3.minedrift.client.animation.v2.interpolation.mixed;

import io.github.salatosik3.minedrift.client.animation.v2.interpolation.EaseInOutInterpolation;
import io.github.salatosik3.minedrift.client.animation.v2.interpolation.Interpolation;

public class Vec2EaseInOutInterpolation extends AbstractVec2Interpolation {
    public Vec2EaseInOutInterpolation(long duration) {
        super(duration);
    }

    @Override
    protected Interpolation getInterpolation(long duration) {
        return new EaseInOutInterpolation(duration);
    }
}

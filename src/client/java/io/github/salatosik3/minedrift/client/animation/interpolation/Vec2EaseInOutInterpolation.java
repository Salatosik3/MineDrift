package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.animation.Clock;
import net.minecraft.world.phys.Vec2;

public class Vec2EaseInOutInterpolation extends AbstractInterpolation<Vec2> {
    public Vec2EaseInOutInterpolation(Clock clock, Vec2 min, Vec2 max, long duration) {
        super(clock, min, max, duration);
    }

    public Vec2EaseInOutInterpolation(Vec2 min, Vec2 max, long duration) {
        super(min, max, duration);
    }

    @Override
    protected Vec2 compute(Vec2 a, Vec2 b, double t) {
        return new Vec2(interpolateSingleVal(a.x, b.x, t), interpolateSingleVal(a.y, b.y, t));
    }

    private float interpolateSingleVal(float a, float b, double t) {
        return (float) (a + (b - a) * t * t * (3 - 2 * t));
    }
}

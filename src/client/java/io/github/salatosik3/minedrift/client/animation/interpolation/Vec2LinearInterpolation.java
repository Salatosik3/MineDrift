package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.animation.Clock;
import net.minecraft.world.phys.Vec2;

public class Vec2LinearInterpolation extends AbstractInterpolation<Vec2> {
    public Vec2LinearInterpolation(Clock clock, Vec2 min, Vec2 max, long duration) {
        super(clock, min, max, duration);
    }

    public Vec2LinearInterpolation(Vec2 min, Vec2 max, long duration) {
        super(min, max, duration);
    }

    @Override
    protected Vec2 compute(Vec2 a, Vec2 b, double t) { // a + (b - a) * t;
        return new Vec2(interpolateSingleVal(a.x, b.x, t), interpolateSingleVal(a.y, b.y, t));
    }

    private float interpolateSingleVal(float a, float b, double t) {
        return a + (b - a) * (float) t;
    }
}

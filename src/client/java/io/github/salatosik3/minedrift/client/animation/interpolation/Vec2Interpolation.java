package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.animation.Clock;
import net.minecraft.world.phys.Vec2;

public class Vec2Interpolation extends AbstractInterpolation<Vec2> {

    public Vec2Interpolation(InterpolationType type, Clock clock, Vec2 min, Vec2 max, long duration) {
        super(type, clock, min, max, duration);
    }

    public Vec2Interpolation(InterpolationType type, Vec2 min, Vec2 max, long duration) {
        super(type, min, max, duration);
    }

    @Override
    protected Vec2 compute(Vec2 a, Vec2 b, double t) {
        return new Vec2(computeSingleValue(a.x, b.x, t), computeSingleValue(a.y, b.y, t));
    }

    private float computeSingleValue(float a, float b, double t) {
        return (float) switch(type) {
            case LINEAR -> a + (b - a) * t;
            case EASE_IN_OUT -> a + (b - a) * t * t * (3 - 2 * t);
        };
    }
}

package io.github.salatosik3.minedrift.client.animation.v2.effect;

import io.github.salatosik3.minedrift.client.animation.v2.interpolation.mixed.Vec2LinearInterpolation;
import net.minecraft.world.phys.Vec2;

import java.util.Random;

public class ShakingEffect implements Effect<Vec2> {
    private final Random random = new Random();
    private final Vec2[] shakePositions = {
            new Vec2(0, 0),
            new Vec2(0.5f, 1),
            new Vec2(1, 0),
            new Vec2(0, 0.5f),
            new Vec2(1, 0.5f),
    };

    private final Vec2LinearInterpolation interpolation;

    private int shakePosIndex = 0;
    private Vec2 min = new Vec2(0, 0), max = new Vec2(1, 1);

    public ShakingEffect(long oneShakeDuration) {
        interpolation = new Vec2LinearInterpolation(oneShakeDuration);
    }

    public ShakingEffect() {
        this(75L);
    }

    private Vec2 uglyVec(Vec2 vec) {
        float uglyX = vec.x == 1 ? vec.x : random.nextFloat(1.0f - vec.x) + vec.x;
        float uglyY = vec.y == 1 ? vec.y : random.nextFloat(1.0f - vec.y) + vec.y;
        return new Vec2(uglyX, uglyY);
    }

    @Override
    public Vec2 animate() {
        Vec2 interpolatedVec = interpolation.interpolate();

        if (interpolation.isFinished()) {
            min = max;
            shakePosIndex += 1;
            shakePosIndex %= shakePositions.length;
            max = uglyVec(shakePositions[shakePosIndex]);
            interpolation.reset();
        }

        float x = min.x + interpolatedVec.x * max.x;
        float y = min.y + interpolatedVec.y * max.y;

        return new Vec2(x, y);
    }
}

package io.github.salatosik3.minedrift.client.animation;

import io.github.salatosik3.minedrift.client.animation.interpolation.Interpolation;
import io.github.salatosik3.minedrift.client.animation.interpolation.Vec2LinearInterpolation;
import net.minecraft.world.phys.Vec2;

import java.util.Random;

public class V2ShakingAnimation implements Animation<Vec2> {

    private final Random random = new Random();
    private final Interpolation<Vec2> interpolation;
    private final Vec2[] shakePositions = {
            new Vec2(0, 0),
            new Vec2(0.5f, 1),
            new Vec2(1, 0),
            new Vec2(0, 0.5f),
            new Vec2(1, 0.5f),
    };

    private int shakePosIndex = 0;

    public V2ShakingAnimation(long shakeDuration) {
        interpolation = new Vec2LinearInterpolation(new Vec2(0, 0), new Vec2(1, 1), shakeDuration);
        interpolation.addOnEndCallback(i ->  {
            i.setMin(i.getMax());

            shakePosIndex += 1;
            shakePosIndex %= shakePositions.length;
            i.setMax(uglyVec(shakePositions[shakePosIndex]));

            i.restart();
        });
    }

    public V2ShakingAnimation() {
        this(75);
    }

    private Vec2 uglyVec(Vec2 vec) {
        float uglyX = vec.x == 1 ? vec.x : random.nextFloat(1.0f - vec.x) + vec.x;
        float uglyY = vec.y == 1 ? vec.y : random.nextFloat(1.0f - vec.y) + vec.y;
        return new Vec2(uglyX, uglyY);
    }

    @Override
    public Vec2 animate() {
        return interpolation.get();
    }
}

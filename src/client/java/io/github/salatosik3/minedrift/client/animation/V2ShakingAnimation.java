package io.github.salatosik3.minedrift.client.animation;

import io.github.salatosik3.minedrift.client.animation.interpolation.Interpolation;
import io.github.salatosik3.minedrift.client.animation.interpolation.Vec2LinearInterpolation;
import net.minecraft.world.phys.Vec2;

import java.util.Random;

public class V2ShakingAnimation implements Animation<Vec2> {

    private final Random random = new Random();
    private final Interpolation<Vec2> interpolation;

    public V2ShakingAnimation() {

        interpolation = new Vec2LinearInterpolation(new Vec2(0, 0), new Vec2(1, 1), 100);
        interpolation.addOnEndCallback(i ->  {
            i.setMin(i.getMax());
            i.setMax(new Vec2(randomFloat(), randomFloat()));
            i.restart();
        });
    }

    @Override
    public Vec2 animate() {
        return interpolation.get();
    }

    private float randomFloat(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    private float randomFloat() {
        return randomFloat(0f, 1f);
    }
}

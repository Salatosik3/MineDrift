package io.github.salatosik3.minedrift.client.animation.v2.interpolation.mixed;

import io.github.salatosik3.minedrift.client.animation.v2.interpolation.GenericInterpolation;
import io.github.salatosik3.minedrift.client.animation.v2.interpolation.Interpolation;
import net.minecraft.world.phys.Vec2;

public abstract class AbstractVec2Interpolation implements GenericInterpolation<Vec2> {

    private final Interpolation xInt, yInt;

    public AbstractVec2Interpolation(long duration) {
        xInt = getInterpolation(duration);
        yInt = getInterpolation(duration);
    }

    protected abstract Interpolation getInterpolation(long duration);

    @Override
    public Vec2 interpolate() {
        return new Vec2(xInt.interpolate(), yInt.interpolate());
    }

    @Override
    public void setReverse(boolean reverse) {
        xInt.setReverse(reverse);
        yInt.setReverse(reverse);
    }

    @Override
    public void reset() {
        xInt.reset();
        yInt.reset();
    }

    @Override
    public boolean isFinished() {
        return xInt.isFinished() && yInt.isFinished();
    }

    @Override
    public void setPause(boolean pause) {
        xInt.setPause(pause);
        yInt.setPause(pause);
    }
}

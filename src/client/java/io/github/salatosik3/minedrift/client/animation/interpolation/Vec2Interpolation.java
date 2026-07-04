package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.animation.Clock;
import net.minecraft.world.phys.Vec2;

public class Vec2Interpolation implements Interpolation<Vec2> {
    private final Clock clock;
    private Type type;
    private Vec2 minVal;
    private Vec2 maxVal;
    private long timeToComplete;
    private Mode mode;

    private final SingleValueInterpolation xInterpolation;
    private final SingleValueInterpolation yInterpolation;

    public Vec2Interpolation(Clock clock, Type type, Vec2 minVal, Vec2 maxVal, long timeToComplete, Mode mode) {
        this.clock = clock;
        this.type = type;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.timeToComplete = timeToComplete;
        this.mode = mode;

        xInterpolation = new SingleValueInterpolation(clock, type, minVal.x, maxVal.x, timeToComplete, mode);
        yInterpolation = new SingleValueInterpolation(clock, type, minVal.y, maxVal.y, timeToComplete, mode);
    }

    public Vec2Interpolation(Type type, Vec2 minVal, Vec2 maxVal, long timeToComplete, Mode mode) {
        this(new Clock(), type, minVal, maxVal, timeToComplete, mode);
    }

    @Override
    public Vec2 compute() {
        return new Vec2((float) (double) xInterpolation.compute(), (float) (double) yInterpolation.compute());
    }

    @Override
    public Clock getClock() {
        return clock;
    }

    @Override
    public void setMode(Mode mode) {
        this.mode = mode;
        xInterpolation.setMode(mode);
        yInterpolation.setMode(mode);
    }

    @Override
    public Mode getMode() {
        return this.mode;
    }

    @Override
    public Vec2 getMinVal() {
        return this.minVal;
    }

    @Override
    public void setMinVal(Vec2 minVal) {
        this.minVal = minVal;
        xInterpolation.setMinVal((double) minVal.x);
        yInterpolation.setMinVal((double) minVal.y);
    }

    @Override
    public Vec2 getMaxVal() {
        return this.maxVal;
    }

    @Override
    public void setMaxVal(Vec2 maxValue) {
        this.maxVal = maxValue;
        xInterpolation.setMaxVal((double) maxValue.x);
        yInterpolation.setMaxVal((double) maxValue.y);
    }

    @Override
    public long getTimeToComplete() {
        return this.timeToComplete;
    }

    @Override
    public void setTimeToComplete(long timeToComplete) {
        this.timeToComplete = timeToComplete;
        xInterpolation.setTimeToComplete(timeToComplete);
        yInterpolation.setTimeToComplete(timeToComplete);
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
        xInterpolation.setType(type);
        yInterpolation.setType(type);
    }

    @Override
    public void reset() {
        xInterpolation.reset();
        yInterpolation.reset();
    }

    @Override
    public boolean isStopped() {
        if (xInterpolation.isStopped() && !yInterpolation.isStopped() || !xInterpolation.isStopped() && yInterpolation.isStopped()) {
            throw new IllegalStateException("Both axes have different states!");
        }
        return xInterpolation.isStopped() && yInterpolation.isStopped(); // God bless you
    }

    @Override
    public void stop() {
        xInterpolation.stop();
        yInterpolation.stop();
    }
}

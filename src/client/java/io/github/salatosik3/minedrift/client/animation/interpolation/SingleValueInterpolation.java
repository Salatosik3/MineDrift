package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.animation.Clock;

public class SingleValueInterpolation implements Interpolation<Double> {

    private final Clock clock;
    private Type type;
    private double minVal;
    private double maxVal;
    private long timeToComplete;
    private Mode mode;

    private long accumulatedTime = 0;
    private boolean reversing = false;
    private boolean stopped = false;

    private boolean innerClock = false;

    public SingleValueInterpolation(Clock clock, Type type, double minVal, double maxVal, long timeToComplete, Mode mode) {
        this.clock = clock;
        this.type = type;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.timeToComplete = timeToComplete;
        this.mode = mode;

        this.innerClock = false;
    }

    public SingleValueInterpolation(Type type, double minVal, double maxVal, long timeToComplete, Mode mode) {
        this(new Clock(), type, minVal, maxVal, timeToComplete, mode);
        this.innerClock = true;
    }

    public Double compute() {
        if (innerClock && !clock.isStarted()) { // I'm not sure if this is a good idea, but looks like it gonna be okay
            clock.start();
        }

        if (stopped) {
            return type.getFunc().interpolate(minVal, maxVal, (double) accumulatedTime / timeToComplete);
        }

        long delay = clock.getDelay();
        accumulatedTime += reversing ? -delay : delay;

        if (accumulatedTime > timeToComplete) {
            switch (mode) {
                case REVERSING_CYCLE -> {
                    reversing = true;
                    accumulatedTime = timeToComplete;
                }
                case CYCLE -> {
                    reversing = false;
                    accumulatedTime = 0;
                }
                case STOP_WHEN_END -> {
                    stopped = true;
                    accumulatedTime = timeToComplete;
                }
            }
        } else if (accumulatedTime < 0) {
            accumulatedTime = 0;
            reversing = false;
        }

        return type.getFunc().interpolate(minVal, maxVal, (double) accumulatedTime / timeToComplete);
    }

    @Override
    public Clock getClock() {
        return clock;
    }

    @Override
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public Mode getMode() {
        return this.mode;
    }

    @Override
    public Double getMinVal() {
        return this.minVal;
    }

    @Override
    public void setMinVal(Double minVal) {
        this.minVal = minVal;
    }

    @Override
    public Double getMaxVal() {
        return this.maxVal;
    }

    @Override
    public void setMaxVal(Double maxVal) {
        this.maxVal = maxVal;
    }

    @Override
    public long getTimeToComplete() {
        return this.timeToComplete;
    }

    @Override
    public void setTimeToComplete(long timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void reset() {
        this.accumulatedTime = 0;
        this.stopped = false;
        this.reversing = false;
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    @Override
    public void stop() {
        this.stopped = true;
    }
}

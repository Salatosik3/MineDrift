package io.github.salatosik3.minedrift.client.motion.interpolation;

import io.github.salatosik3.minedrift.client.motion.Clock;

public abstract class AbstractInterpolation implements Interpolation {

    private final Clock clock = new Clock();
    private final long duration;

    private boolean reverse = false;
    private boolean pause = false;
    private long accumulatedTime = 0;

    public AbstractInterpolation(long duration) {
        this.duration = duration;
    }

    protected abstract float compute(float a, float b, float t);

    protected float compute(float t) {
        return reverse ? compute(1.0f, 0.0f, t) : compute(0.0f, 1.0f, t);
    }

    @Override
    public Float interpolate() {
        if (!pause && !clock.isStarted()) {
            clock.start();
        }
        accumulatedTime += clock.getDelay();

        if (accumulatedTime > duration) {
            accumulatedTime = duration;
            clock.stop();
        }

        return compute((float) accumulatedTime / duration);
    }

    @Override
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public void reset() {
        clock.stop();
        accumulatedTime = 0;
    }

    @Override
    public boolean isFinished() {
        return accumulatedTime == duration && !clock.isStarted();
    }

    @Override
    public void setPause(boolean pause) {
        this.pause = pause;
    }
}

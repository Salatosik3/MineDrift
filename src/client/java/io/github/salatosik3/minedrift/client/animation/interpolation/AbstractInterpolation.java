package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.animation.Clock;

public abstract class AbstractInterpolation <T> implements Interpolation<T> {

    protected InterpolationType type;
    private final Clock clock;
    private T min, max;
    private long duration;

    private long acumulatedTime = 0;
    private boolean ended = false;
    private boolean reverse = false;

    public AbstractInterpolation(InterpolationType type, Clock clock, T min, T max, long duration) {
        this.clock = clock;
        this.min = min;
        this.max = max;
        this.duration = duration;
    }

    @Override
    public T get() {
        if (ended) {
            return compute(min, max, (double) acumulatedTime / duration);
        }

        long delay = clock.getDelay();
        acumulatedTime += reverse ? -delay : delay;

        if (acumulatedTime > delay) {
            ended = true;
            acumulatedTime = delay;
        } else if (acumulatedTime < 0) {
            ended = true;
            acumulatedTime = 0;
        }

        return compute(min, max, (double) acumulatedTime / duration);
    }

    protected abstract T compute(T a, T b, double t);

    @Override
    public boolean isEnded() {
        return ended;
    }

    @Override
    public void restart() {
        ended = false;
        acumulatedTime = reverse ? duration : 0;
    }

    @Override
    public boolean isReverse() {
        return reverse;
    }

    @Override
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public InterpolationType getType() {
        return type;
    }

    @Override
    public void setType(InterpolationType type) {
        this.type = type;
    }
}

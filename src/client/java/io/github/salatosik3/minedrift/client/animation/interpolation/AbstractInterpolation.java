package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.MineDriftClient;
import io.github.salatosik3.minedrift.client.animation.Clock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractInterpolation <T> implements Interpolation<T> {

    private final Clock clock;
    private T min, max;
    private long duration;

    private long acumulatedTime = 0;
    private boolean ended = false;
    private boolean reverse = false;

    private boolean selfCreatedClock;

    private final List<Consumer<Interpolation<T>>> callbacks = new ArrayList<>();

    public AbstractInterpolation(Clock clock, T min, T max, long duration) {
        this.clock = clock;
        this.min = min;
        this.max = max;
        this.duration = duration;
        selfCreatedClock = false;
    }

    public AbstractInterpolation(T min, T max, long duration) {
        this(new Clock(), min, max, duration);
        selfCreatedClock = true;
    }

    @Override
    public boolean isEnded() {
        return ended;
    }

    @Override
    public void restart() {
        ended = false;
        acumulatedTime = reverse ? duration : 0;
        if (selfCreatedClock) {
            clock.stop();
            clock.start();
        }
    }

    @Override
    public T get() {
        if (ended) {
            return reverse ? min : max;
        }

        if (!clock.isStarted() && selfCreatedClock) {
            clock.start();
        }

        long delay = clock.getDelay();
        acumulatedTime += reverse ? -delay : delay;

        if (acumulatedTime > duration || acumulatedTime < 0) {
            ended = true;
            if (selfCreatedClock) {
                clock.stop();
            }
            callbacks.forEach(callback -> callback.accept(this));
        }

        return compute(min, max, (double) acumulatedTime / duration);
    }

    protected abstract T compute(T a, T b, double t);

    @Override
    public boolean isReverse() {
        return reverse;
    }

    @Override
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public T getMin() {
        return min;
    }

    @Override
    public void setMax(T max) {
        this.max = max;
    }

    @Override
    public T getMax() {
        return max;
    }

    @Override
    public void setMin(T min) {
        this.min = min;
    }

    @Override
    public void addOnEndCallback(Consumer<Interpolation<T>> callback) {
        callbacks.add(callback);
    }
}

package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.MineDriftClient;
import io.github.salatosik3.minedrift.client.animation.Clock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractInterpolation <T> implements Interpolation<T> {

    protected InterpolationType type;
    private final Clock clock;
    private T min, max;
    private long duration;

    private long acumulatedTime = 0;
    private boolean ended = false;
    private boolean reverse = false;

    private boolean selfCreatedClock;

    private final List<Consumer<Interpolation<T>>> callbacks = new ArrayList<>();

    public AbstractInterpolation(InterpolationType type, Clock clock, T min, T max, long duration) {
        this.type = type;
        this.clock = clock;
        this.min = min;
        this.max = max;
        this.duration = duration;
        selfCreatedClock = false;
    }

    public AbstractInterpolation(InterpolationType type, T min, T max, long duration) {
        this(type, new Clock(), min, max, duration);
        selfCreatedClock = true;
    }

    @Override
    public T get() {
        if (!clock.isStarted() && selfCreatedClock) {
            clock.start();
        }

        if (ended) {
            return reverse ? min : max;
        }

        long delay = clock.getDelay();
        acumulatedTime += reverse ? -delay : delay;

        boolean moreThanDuration = acumulatedTime > duration;
        boolean lessThanZero = acumulatedTime < 0;

        if (moreThanDuration || lessThanZero) {
            if (selfCreatedClock) {
                clock.stop();
            }

            if (moreThanDuration) acumulatedTime = duration;
            else acumulatedTime = 0;
        }

        return compute(min, max, (double) acumulatedTime / duration);
    }

    protected abstract T compute(T a, T b, double t);

    @Override
    public void addOnEndCallback(Consumer<Interpolation<T>> callback) {
        callbacks.add(callback);
    }

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

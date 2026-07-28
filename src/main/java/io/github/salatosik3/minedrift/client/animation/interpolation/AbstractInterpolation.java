package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.animation.Clock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractInterpolation <T> implements Interpolation<T> {

    private final Clock clock = new Clock();
    private T min, max;
    private long duration;

    private long acumulatedTime = 0;
    private boolean finished = false;
    private boolean stopped = false;
    private boolean reverse = false;

    private final List<Consumer<Interpolation<T>>> callbacks = new ArrayList<>();

    public AbstractInterpolation(T min, T max, long duration) {
        this.min = min;
        this.max = max;
        this.duration = duration;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    @Override
    public void start() {
        stopped = false;
        clock.start();
    }

    @Override
    public void stop() {
        stopped = true;
        clock.stop();
    }

    @Override
    public void restart() {
        stopped = false;
        finished = false;
        acumulatedTime = reverse ? duration : 0;
        clock.stop();
        clock.start();
    }

    @Override
    public void reset() {
        acumulatedTime = 0;
        finished = false;
        stopped = false;
        reverse = false;
        clock.stop();
    }

    @Override
    public T get() {
        if (finished) {
            return reverse ? min : max;
        }

        if (stopped) {
            return compute(min, max, (double) acumulatedTime / duration);
        }

        if (!clock.isStarted()) {
            clock.start();
        }

        long delay = clock.getDelay();
        acumulatedTime += reverse ? -delay : delay;

        if (acumulatedTime > duration || acumulatedTime < 0) {
            finished = true;
            clock.stop();
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

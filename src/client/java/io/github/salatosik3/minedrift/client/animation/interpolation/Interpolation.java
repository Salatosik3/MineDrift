package io.github.salatosik3.minedrift.client.animation.interpolation;

import io.github.salatosik3.minedrift.client.animation.Clock;

public interface Interpolation <T> {
    T compute();

    Clock getClock();

    void setMode(Mode mode);

    Mode getMode();

    T getMinVal();

    void setMinVal(T minVal);

    T getMaxVal();

    void setMaxVal(T maxValue);

    long getTimeToComplete();

    void setTimeToComplete(long timeToComplete);

    Type getType();

    void setType(Type type);

    void reset();

    boolean isStopped();

    void stop();
}

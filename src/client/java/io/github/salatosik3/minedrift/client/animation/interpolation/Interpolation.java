package io.github.salatosik3.minedrift.client.animation.interpolation;

import java.util.function.Consumer;

public interface Interpolation <T> {
    T get();

    boolean isEnded();

    void restart();

    boolean isReverse();

    void setReverse(boolean reverse);

    void addOnEndCallback(Consumer<Interpolation<T>> callback);

    T getMin();

    void setMin(T min);

    T getMax();

    void setMax(T max);
}

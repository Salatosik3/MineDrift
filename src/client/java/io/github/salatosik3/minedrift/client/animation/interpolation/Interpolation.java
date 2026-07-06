package io.github.salatosik3.minedrift.client.animation.interpolation;

import java.util.function.Consumer;

public interface Interpolation <T> {
    T get();

    boolean isEnded();

    void restart();

    boolean isReverse();

    void setReverse(boolean reverse);

    InterpolationType getType();

    void setType(InterpolationType type);

    void addOnEndCallback(Consumer<Interpolation<T>> callback);
}

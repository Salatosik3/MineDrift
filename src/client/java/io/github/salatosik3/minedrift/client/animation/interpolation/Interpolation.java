package io.github.salatosik3.minedrift.client.animation.interpolation;

public interface Interpolation <T> {
    T get();

    boolean isEnded();

    void restart();

    boolean isReverse();

    void setReverse(boolean reverse);

    InterpolationType getType();

    void setType(InterpolationType type);
}

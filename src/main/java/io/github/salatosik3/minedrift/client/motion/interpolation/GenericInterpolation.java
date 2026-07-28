package io.github.salatosik3.minedrift.client.motion.interpolation;

public interface GenericInterpolation<T> {
    T interpolate();

    void setReverse(boolean reverse);

    void reset();

    boolean isFinished();

    void setPause(boolean pause);
}

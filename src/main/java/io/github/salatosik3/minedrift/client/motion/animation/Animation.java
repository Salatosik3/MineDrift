package io.github.salatosik3.minedrift.client.motion.animation;

public interface Animation<T> {
    T animate();

    void setReverse(boolean reverse);

    boolean isFinished();

    void reset();
}

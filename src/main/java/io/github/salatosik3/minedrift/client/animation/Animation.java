package io.github.salatosik3.minedrift.client.animation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface Animation <T> {
    T animate();

    boolean isFinished();

    boolean isStopped();

    void start();

    void stop();

    void restart();

    void reset();

    void setReverse(boolean reverse);
}

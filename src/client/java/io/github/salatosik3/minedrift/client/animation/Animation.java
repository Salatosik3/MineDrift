package io.github.salatosik3.minedrift.client.animation;

import java.util.function.Consumer;

public interface Animation <T> {
    T animate();

    default void animate(Consumer<T> consumer) {
        consumer.accept(animate());
    }
}

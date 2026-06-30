package io.github.salatosik3.testing.event;

import io.github.salatosik3.testing.event.data.Event;

import java.util.function.Consumer;

public interface EventListenerRegistrar {
    <T extends Event> void register(Class<T> clazz, Consumer<T> listener);
}

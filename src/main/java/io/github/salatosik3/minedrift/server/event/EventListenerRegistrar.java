package io.github.salatosik3.minedrift.server.event;

import io.github.salatosik3.minedrift.server.event.data.Event;

import java.util.function.Consumer;

public interface EventListenerRegistrar {
    <T extends Event> void register(Class<T> clazz, Consumer<T> listener);
}

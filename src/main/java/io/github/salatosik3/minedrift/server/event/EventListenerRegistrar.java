package io.github.salatosik3.minedrift.server.event;

import io.github.salatosik3.minedrift.server.event.data.Event;

import java.util.function.Consumer;

public interface EventListenerRegistrar {
    void register(Class<? extends Event> clazz, Consumer<? extends Event> listener);

    default void register(BusEventListener busEventListener) {
        busEventListener.getListeners().forEach(listenerEntry ->
                register(listenerEntry.clazz, listenerEntry.consumer)
        );
    }
}

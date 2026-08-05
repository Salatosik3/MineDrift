package io.github.salatosik3.minedrift.server.event;

import io.github.salatosik3.minedrift.server.event.data.Event;

import java.util.function.Consumer;

public class ListenerEntry <T extends Event> {
    protected final Class<T> clazz;
    protected final Consumer<T> consumer;

    public ListenerEntry(Class<T> clazz, Consumer<T> consumer) {
        this.clazz = clazz;
        this.consumer = consumer;
    }
}

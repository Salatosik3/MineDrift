package io.github.salatosik3.minedrift.event;

import io.github.salatosik3.minedrift.event.data.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus implements EventListenerRegistrar, ListenerInvoker {

    private final Map<Class<?>, List<Consumer<Event>>> listeners = new HashMap<>();

    @Override
    public <T extends Event> void register(Class<T> clazz, Consumer<T> listener) {
        var listeners = this.listeners.computeIfAbsent(clazz, _ -> new ArrayList<>());
        listeners.add((Consumer<Event>) listener);
    }

    @Override
    public <T extends Event> void invoke(T event) {
        var listeners = this.listeners.get(event.getClass());
        if (listeners != null) {
            for (var listener : listeners) {
                listener.accept(event);
            }
        }
    }
}

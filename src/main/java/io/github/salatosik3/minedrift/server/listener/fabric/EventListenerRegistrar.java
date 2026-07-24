package io.github.salatosik3.minedrift.server.listener.fabric;

import io.github.salatosik3.minedrift.server.event.EventBus;
import io.github.salatosik3.minedrift.server.listener.fabric.drift.BoatDriftListener;

import java.util.ArrayList;
import java.util.List;

public class EventListenerRegistrar {
    private final List<EventListener> listeners = new ArrayList<>();
    private final EventBus eventBus;

    public EventListenerRegistrar(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    private void register(EventListener listener) {
        listener.register();
        listeners.add(listener);
    }

    public void registerAll() {
        register(new BoatDriftListener(eventBus));
    }
}

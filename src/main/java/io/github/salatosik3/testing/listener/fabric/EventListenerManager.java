package io.github.salatosik3.testing.listener.fabric;

import io.github.salatosik3.testing.event.EventBus;
import io.github.salatosik3.testing.listener.fabric.drift.BoatDriftListener;

import java.util.ArrayList;
import java.util.List;

public class EventListenerManager {
    private final List<EventListener> listeners = new ArrayList<>();
    private final EventBus eventBus;

    public EventListenerManager(EventBus eventBus) {
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

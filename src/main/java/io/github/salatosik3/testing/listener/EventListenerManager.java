package io.github.salatosik3.testing.listener;

import io.github.salatosik3.testing.listener.drift.BoatDriftListener;

import java.util.ArrayList;
import java.util.List;

public class EventListenerManager {
    private final List<EventListener> listeners = new ArrayList<>();

    private void register(EventListener listener) {
        listener.register();
        listeners.add(listener);
    }

    public void registerAll() {
        register(new BoatDriftListener());
    }
}

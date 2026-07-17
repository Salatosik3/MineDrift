package io.github.salatosik3.minedrift.listener.bus;

import io.github.salatosik3.minedrift.event.EventListenerRegistrar;
import io.github.salatosik3.minedrift.event.data.BoatDriftEvent;

// TODO I have to improve naming for this kind of classes in my project...
public class BusEventListenerManager {
    private final EventListenerRegistrar registrar;

    public BusEventListenerManager(EventListenerRegistrar registrar) {
        this.registrar = registrar;
    }

    public void registerAll() {
        registrar.register(BoatDriftEvent.class, new BoatDriftListener());
    }
}

package io.github.salatosik3.minedrift.listener.bus;

import io.github.salatosik3.minedrift.event.EventListenerRegistrar;
import io.github.salatosik3.minedrift.event.data.BoatDriftEvent;

public class BusEventListenerRegistrar {
    private final EventListenerRegistrar registrar;

    public BusEventListenerRegistrar(EventListenerRegistrar registrar) {
        this.registrar = registrar;
    }

    public void registerAll() {
        registrar.register(BoatDriftEvent.class, new BoatDriftListener());
    }
}

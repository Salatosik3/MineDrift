package io.github.salatosik3.minedrift.server.listener.bus;

import io.github.salatosik3.minedrift.server.event.EventListenerRegistrar;
import io.github.salatosik3.minedrift.server.event.data.BoatCollisionEvent;
import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.service.DriftPacketService;
import io.github.salatosik3.minedrift.server.service.ServiceRegistrar;

public class BusEventListenerRegistrar {
    private final EventListenerRegistrar registrar;
    private final ServiceRegistrar serviceRegistrar;

    public BusEventListenerRegistrar(EventListenerRegistrar registrar, ServiceRegistrar serviceRegistrar) {
        this.registrar = registrar;
        this.serviceRegistrar = serviceRegistrar;
    }

    public void registerAll() {
        // TODO kind of useless if I use them only to call an another method from an another class, at least I should group them or something
        registrar.register(BoatDriftEvent.class, new BoatDriftListener(serviceRegistrar.get(DriftPacketService.class)));
        registrar.register(BoatCollisionEvent.class, new BoatCollisionListener(serviceRegistrar.get(DriftPacketService.class)));
    }
}

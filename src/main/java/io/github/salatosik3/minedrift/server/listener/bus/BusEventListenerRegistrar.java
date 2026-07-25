package io.github.salatosik3.minedrift.server.listener.bus;

import io.github.salatosik3.minedrift.server.event.EventListenerRegistrar;
import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.service.DriftingPacketService;
import io.github.salatosik3.minedrift.server.service.ServiceRegistrar;

public class BusEventListenerRegistrar {
    private final EventListenerRegistrar registrar;
    private final ServiceRegistrar serviceRegistrar;

    public BusEventListenerRegistrar(EventListenerRegistrar registrar, ServiceRegistrar serviceRegistrar) {
        this.registrar = registrar;
        this.serviceRegistrar = serviceRegistrar;
    }

    public void registerAll() {
        registrar.register(BoatDriftEvent.class, new BoatDriftListener(serviceRegistrar.get(DriftingPacketService.class)));
    }
}

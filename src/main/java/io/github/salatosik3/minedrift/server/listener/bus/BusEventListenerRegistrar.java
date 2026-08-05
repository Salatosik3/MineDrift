package io.github.salatosik3.minedrift.server.listener.bus;

import io.github.salatosik3.minedrift.server.event.EventListenerRegistrar;
import io.github.salatosik3.minedrift.server.service.DriftPacketService;
import io.github.salatosik3.minedrift.server.service.DriftScoreService;
import io.github.salatosik3.minedrift.server.service.ServiceRegistrar;

public class BusEventListenerRegistrar {
    private final EventListenerRegistrar registrar;
    private final ServiceRegistrar serviceRegistrar;

    public BusEventListenerRegistrar(EventListenerRegistrar registrar, ServiceRegistrar serviceRegistrar) {
        this.registrar = registrar;
        this.serviceRegistrar = serviceRegistrar;
    }

    public void registerAll() {
        registrar.register(new DriftListener(serviceRegistrar.get(DriftPacketService.class), serviceRegistrar.get(DriftScoreService.class)));
    }
}

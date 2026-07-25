package io.github.salatosik3.minedrift.server.service;

import io.github.salatosik3.minedrift.misc.Registrar;
import io.github.salatosik3.minedrift.server.service.impl.DriftingPacketServiceImpl;

public class ServiceRegistrar extends Registrar {
    public void registerAll() {
        register(DriftingPacketService.class, new DriftingPacketServiceImpl());
    }
}

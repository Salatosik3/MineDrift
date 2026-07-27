package io.github.salatosik3.minedrift.server.service;

import io.github.salatosik3.minedrift.misc.Registrar;
import io.github.salatosik3.minedrift.server.service.impl.DriftPacketServiceImpl;
import io.github.salatosik3.minedrift.server.timer.TimerProvider;

public class ServiceRegistrar extends Registrar {

    private final TimerProvider timerProvider;

    public ServiceRegistrar(TimerProvider timerProvider) {
        this.timerProvider = timerProvider;
    }

    public void registerAll() {
        register(DriftPacketService.class, new DriftPacketServiceImpl(timerProvider.getTimer()));
    }
}

package io.github.salatosik3.minedrift.server.listener.bus;

import io.github.salatosik3.minedrift.server.event.data.BoatCollisionEvent;
import io.github.salatosik3.minedrift.server.service.DriftPacketService;

import java.util.function.Consumer;

public class BoatCollisionListener implements Consumer<BoatCollisionEvent> {
    private final DriftPacketService driftPacketService;

    public BoatCollisionListener(DriftPacketService driftPacketService) {
        this.driftPacketService = driftPacketService;
    }

    @Override
    public void accept(BoatCollisionEvent boatCollisionEvent) {
        driftPacketService.notifyCollision(boatCollisionEvent.getServerPlayer(), boatCollisionEvent.getBoat());
    }
}

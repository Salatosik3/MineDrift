package io.github.salatosik3.minedrift.server.listener.bus;

import io.github.salatosik3.minedrift.server.event.BusEventListener;
import io.github.salatosik3.minedrift.server.event.ListenerEntry;
import io.github.salatosik3.minedrift.server.event.data.BoatCollisionEvent;
import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.event.data.Event;
import io.github.salatosik3.minedrift.server.service.DriftPacketService;
import io.github.salatosik3.minedrift.server.service.DriftScoreService;

import java.util.List;

public class DriftListener implements BusEventListener {
    private final DriftPacketService driftPacketService;
    private final DriftScoreService driftScoreService;

    public DriftListener(DriftPacketService driftPacketService, DriftScoreService driftScoreService) {
        this.driftPacketService = driftPacketService;
        this.driftScoreService = driftScoreService;
    }

    @Override
    public List<ListenerEntry<? extends Event>> getListeners() {
        return List.of(
                new ListenerEntry<>(BoatDriftEvent.class, this::notifyServicesWhenBoatDrifts),
                new ListenerEntry<>(BoatCollisionEvent.class, this::notifyServicesWhenBoatCollides)
        );
    }

    private void notifyServicesWhenBoatDrifts(BoatDriftEvent event) {
        int points = driftScoreService.calculatePoints(event.getServerPlayer(), event.getBoat(), event.getBoatVelocity(), event.getDriftAngle());
        driftPacketService.notifyDrifting(event.getServerPlayer(), points);
    }

    private void notifyServicesWhenBoatCollides(BoatCollisionEvent event) {
        driftPacketService.notifyCollision(event.getServerPlayer());
    }
}

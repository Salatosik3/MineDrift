package io.github.salatosik3.minedrift.server.listener.bus;

import io.github.salatosik3.minedrift.server.event.BusEventListener;
import io.github.salatosik3.minedrift.server.event.ListenerEntry;
import io.github.salatosik3.minedrift.server.event.data.BoatCollisionEvent;
import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.event.data.Event;
import io.github.salatosik3.minedrift.server.service.DriftPacketService;
import io.github.salatosik3.minedrift.server.service.DriftScoreService;
import io.github.salatosik3.minedrift.server.service.DriftService;

import java.util.List;

public class DriftListener implements BusEventListener {
    private final DriftService driftService;

    public DriftListener(DriftService driftService) {
        this.driftService = driftService;
    }

    @Override
    public List<ListenerEntry<? extends Event>> getListeners() {
        return List.of(
                new ListenerEntry<>(BoatDriftEvent.class, this::notifyServicesWhenBoatDrifts),
                new ListenerEntry<>(BoatCollisionEvent.class, this::notifyServicesWhenBoatCollides)
        );
    }

    private void notifyServicesWhenBoatDrifts(BoatDriftEvent event) {
        driftService.notifyDrifting(event.getServerPlayer(), event.getBoat(), event.getBoatVelocity(), event.getDriftAngle());
    }

    private void notifyServicesWhenBoatCollides(BoatCollisionEvent event) {
        driftService.notifyCollision(event.getServerPlayer(), event.getBoat());
    }
}

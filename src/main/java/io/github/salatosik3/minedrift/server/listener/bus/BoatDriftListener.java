package io.github.salatosik3.minedrift.server.listener.bus;

import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.service.DriftPacketService;
import io.github.salatosik3.minedrift.server.service.DriftScoreService;

import java.util.function.Consumer;

public class BoatDriftListener implements Consumer<BoatDriftEvent> {
    private final DriftPacketService driftPacketService;
    private final DriftScoreService driftScoreService;

    public BoatDriftListener(DriftPacketService driftPacketService, DriftScoreService driftScoreService) {
        this.driftPacketService = driftPacketService;
        this.driftScoreService = driftScoreService;
    }

    @Override
    public void accept(BoatDriftEvent event) {
        int points = driftScoreService.calculatePoints(event.getServerPlayer(), event.getBoat(), event.getBoatVelocity(), event.getDriftAngle());
        driftPacketService.notifyDrifting(event.getServerPlayer(), points);
    }
}

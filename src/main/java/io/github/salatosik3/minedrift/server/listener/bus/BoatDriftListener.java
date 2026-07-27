package io.github.salatosik3.minedrift.server.listener.bus;

import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.service.DriftPacketService;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class BoatDriftListener implements Consumer<BoatDriftEvent> {
    private final DriftPacketService driftPacketService;

    public BoatDriftListener(DriftPacketService driftPacketService) {
        this.driftPacketService = driftPacketService;
    }

    @Override
    public void accept(BoatDriftEvent boatDriftEvent) {
        boatDriftEvent.getServerPlayer().sendOverlayMessage(Component.literal("You're drifting")); // TODO remove
        driftPacketService.notifyPlayerDrifting(boatDriftEvent.getServerPlayer(), boatDriftEvent.getDriftAngle(), boatDriftEvent.getBoat(), boatDriftEvent.getBoatVelocity());
    }
}

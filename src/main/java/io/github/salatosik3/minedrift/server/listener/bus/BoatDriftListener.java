package io.github.salatosik3.minedrift.server.listener.bus;

import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.service.DriftPacketService;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class BoatDriftListener implements Consumer<BoatDriftEvent> {
    private final DriftPacketService driftPacketService;

    public BoatDriftListener(DriftPacketService driftPacketService) {
        this.driftPacketService = driftPacketService;
    }

    @Override
    public void accept(BoatDriftEvent boatDriftEvent) {
        driftPacketService.notifyPlayerDrifting(boatDriftEvent.getServerPlayer(), boatDriftEvent.getDriftAngle(),
                boatDriftEvent.getBoat(), boatDriftEvent.getBoatVelocity());
    }
}

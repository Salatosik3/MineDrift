package io.github.salatosik3.minedrift.server.service.impl;

import io.github.salatosik3.minedrift.server.service.DriftPacketService;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class DriftPacketServiceImpl implements DriftPacketService {

    private final Map<UUID, DriftingData> playerData = new HashMap<>();

    public DriftPacketServiceImpl(Timer timer) {

    }

    @Override
    public void notifyPlayerDrifting(ServerPlayer player, double angle) {

    }

    @Override
    public void notifyCollisionDuringDrifting(ServerPlayer player) {

    }

    private record DriftingData(long lastDriftTime, int lastDriftScore) {}
}

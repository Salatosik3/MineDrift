package io.github.salatosik3.minedrift.server.service.impl;

import io.github.salatosik3.minedrift.server.service.DriftingPacketService;
import net.minecraft.server.level.ServerPlayer;

public class DriftingPacketServiceImpl implements DriftingPacketService {
    @Override
    public void notifyPlayerDrift(ServerPlayer player, double angle) {
        // TODO blah blah blah (do some work, send packet blah blah)
    }
}

package io.github.salatosik3.minedrift.server.service;

import net.minecraft.server.level.ServerPlayer;

public interface DriftingPacketService {
    void notifyPlayerDrift(ServerPlayer player, double angle);
}

package io.github.salatosik3.minedrift.server.service;

import net.minecraft.server.level.ServerPlayer;

public interface DriftPacketService {
    void notifyPlayerDrifting(ServerPlayer player, double angle);

    void notifyCollisionDuringDrifting(ServerPlayer player);
}

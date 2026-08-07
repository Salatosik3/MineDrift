package io.github.salatosik3.minedrift.server.service;

import net.minecraft.server.level.ServerPlayer;

public interface DriftPacketService {
    void notifyDrifting(ServerPlayer player, int score);

    void notifyFail(ServerPlayer player);

    void notifyEndDrifting(ServerPlayer player);
}

package io.github.salatosik3.minedrift.server.service;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface DriftPacketService {
    void notifyDrifting(ServerPlayer player, int score);

    void notifyCollision(ServerPlayer player);
}

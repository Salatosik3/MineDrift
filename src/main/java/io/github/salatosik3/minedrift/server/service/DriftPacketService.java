package io.github.salatosik3.minedrift.server.service;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface DriftPacketService {
    void notifyPlayerDrifting(ServerPlayer player, double angle, Entity boat, Vec3 velocity);

    void notifyCollisionDuringDrifting(ServerPlayer player);
}

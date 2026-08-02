package io.github.salatosik3.minedrift.server.service;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface DriftPacketService {
    void notifyDrifting(ServerPlayer player, double angle, Entity vehicle, Vec3 velocity);

    void notifyCollision(ServerPlayer player, Entity vehicle);
}

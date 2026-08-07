package io.github.salatosik3.minedrift.server.service;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface DriftScoreService {
    int calculateScore(ServerPlayer player, Entity vehicle, Vec3 velocity, double angle);

    void resetScore(ServerPlayer player, Entity vehicle);

    int getTotalScore(ServerPlayer player);
}

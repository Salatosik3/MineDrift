package io.github.salatosik3.minedrift.server.service.impl;

import io.github.salatosik3.minedrift.server.service.DriftScoreService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DriftScoreServiceImpl implements DriftScoreService {
    @Override
    public int calculatePoints(ServerPlayer player, Entity vehicle, Vec3 velocity, double angle) {
        return 0;
    }
}

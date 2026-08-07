package io.github.salatosik3.minedrift.server.service.impl;

import io.github.salatosik3.minedrift.server.service.DriftService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DriftServiceImpl implements DriftService {
    @Override
    public void notifyDrifting(ServerPlayer player, Entity vehicle, Vec3 velocity, double angle) {

    }

    @Override
    public void notifyCollision(ServerPlayer player, Entity vehicle) {

    }
}

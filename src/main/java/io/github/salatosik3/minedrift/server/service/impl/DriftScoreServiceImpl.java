package io.github.salatosik3.minedrift.server.service.impl;

import io.github.salatosik3.minedrift.server.service.DriftScoreService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DriftScoreServiceImpl implements DriftScoreService {

    private final Map<UUID, Integer> accumulatedPoints = new HashMap<>();

    @Override
    public int calculatePoints(Entity vehicle, Vec3 velocity, double angle) {
        int points = accumulatedPoints.computeIfAbsent(vehicle.getUUID(), _ -> 0);
        points += (int) Math.round(angle * velocity.length());
        accumulatedPoints.put(vehicle.getUUID(), points);
        return points;
    }

    @Override
    public void resetPoints(Entity vehicle) {
        accumulatedPoints.remove(vehicle.getUUID());
    }
}

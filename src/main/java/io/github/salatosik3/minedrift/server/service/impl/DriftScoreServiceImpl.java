package io.github.salatosik3.minedrift.server.service.impl;

import io.github.salatosik3.minedrift.server.service.DriftScoreService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DriftScoreServiceImpl implements DriftScoreService { // TODO later here some way to save a data has to be added.

    private final Map<BiUUID, Integer> scoreMap = new HashMap<>();
    private final Map<UUID, Integer> totalScoreMap = new HashMap<>();

    @Override
    public int calculateScore(ServerPlayer player, Entity vehicle, Vec3 velocity, double angle) {
        var biUUID = new BiUUID(player.getUUID(), vehicle.getUUID());
        int score = scoreMap.computeIfAbsent(biUUID, _ -> 0);
        score += (int) Math.round(angle * velocity.length());
        scoreMap.put(biUUID, score);
        return score;
    }

    @Override
    public void resetScore(ServerPlayer player, Entity vehicle) {
        var biUUID = new BiUUID(player.getUUID(), vehicle.getUUID());
        Integer score = scoreMap.get(biUUID);

        if (score == null) {
            return;
        }

        int totalScore = totalScoreMap.computeIfAbsent(player.getUUID(), _ -> 0);
        totalScore += score;
        totalScoreMap.put(player.getUUID(), totalScore);
        scoreMap.put(biUUID, 0);
    }

    @Override
    public int getTotalScore(ServerPlayer player) {
        Integer totalScore = totalScoreMap.get(player.getUUID());

        if (totalScore == null) {
            return 0;
        }

        return totalScore;
    }

    private record BiUUID(UUID playerUUID, UUID vehicleUUID) {}
}

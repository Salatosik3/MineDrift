package io.github.salatosik3.minedrift.server.service.impl;

import io.github.salatosik3.minedrift.misc.SimpleTimerTask;
import io.github.salatosik3.minedrift.server.service.DriftPacketService;
import io.github.salatosik3.minedrift.server.service.DriftScoreService;
import io.github.salatosik3.minedrift.server.service.DriftService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class DriftServiceImpl implements DriftService, SimpleTimerTask { // TODO this is fucking suck (and this meme you know)
    // TODO also add some last state check, it won't spam the same status like FAIL too much
    private final DriftScoreService scoreService;
    private final DriftPacketService packetService;
    private final PlayerList playerList;

    private final Map<UUID, DriftData> driftDataMap = new HashMap<>();

    public DriftServiceImpl(DriftScoreService scoreService, DriftPacketService packetService, Timer timer, PlayerList playerList) {
        this.scoreService = scoreService;
        this.packetService = packetService;
        this.playerList = playerList;
        timer.scheduleAtFixedRate(this.asTimerTaskClass(), 0, 500);
    }

    @Override
    public void notifyDrifting(ServerPlayer player, Entity vehicle, Vec3 velocity, double angle) {
        var driftData = driftDataMap.computeIfAbsent(player.getUUID(), _ -> new DriftData(vehicle));

        if (driftData.cooldownTimeMillis != 0) {
            return;
        }

        int score = scoreService.calculateScore(player, vehicle, velocity, angle);
        packetService.notifyDrifting(player, score);

        driftData.lastDriftTimeMillis = System.currentTimeMillis();
    }

    private boolean integerEquals(Vec3 first, Vec3 second) {
        return Math.round(first.x) == Math.round(second.x) &&
                Math.round(first.y) == Math.round(second.y) && Math.round(first.z) == Math.round(second.z);
    }

    @Override
    public void notifyCollision(ServerPlayer player, Entity vehicle) {
        var driftData = driftDataMap.computeIfAbsent(player.getUUID(), _ -> new DriftData(vehicle));

        if (driftData.cooldownTimeMillis != 0) {
            return;
        }

        scoreService.resetScore(player, vehicle);
        packetService.notifyFail(player);

        driftData.cooldownTimeMillis = System.currentTimeMillis();
        driftData.lastDriftTimeMillis = 0;
    }

    @Override
    public void run() {
        final long currentTimeMillis = System.currentTimeMillis();
        List<UUID> driftDataToRemove = new ArrayList<>();

        driftDataMap.forEach((uuid, driftData) -> {
            ServerPlayer player = playerList.getPlayer(uuid);
            if (player == null) {
                driftDataToRemove.add(uuid);
                return;
            }

            if (driftData.lastDriftTimeMillis != 0) {
                long delay = currentTimeMillis - driftData.lastDriftTimeMillis;
                if (delay > 3000) {
                    packetService.notifyEndDrifting(player);
                    scoreService.resetScore(player, driftData.vehicle);
                    driftDataToRemove.add(uuid);
                }
            } else {
                long delay = currentTimeMillis - driftData.cooldownTimeMillis;
                if (delay > 1500L) {
                    driftData.cooldownTimeMillis = 0;
                }
            }
        });

        driftDataToRemove.forEach(driftDataMap::remove);
    }

    private static class DriftData {
        long lastDriftTimeMillis = 0;
        long cooldownTimeMillis = 0;
        final Entity vehicle;

        public DriftData(Entity vehicle) {
            this.vehicle = vehicle;
        }
    }
}

package io.github.salatosik3.minedrift.server.service.impl;

import io.github.salatosik3.minedrift.misc.SimpleTimerTask;
import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import io.github.salatosik3.minedrift.networking.client.DriftState;
import io.github.salatosik3.minedrift.networking.client.DriftStatePayload;
import io.github.salatosik3.minedrift.server.service.DriftPacketService;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DriftPacketServiceImpl implements DriftPacketService, SimpleTimerTask {
    public static final long MAX_DRIFT_DELAY = 3000L;

    private final PlayerList playerList;
    private final Map<UUID, DriftData> playerDriftData = new ConcurrentHashMap<>();

    public DriftPacketServiceImpl(Timer timer, PlayerList playerList) {
        this.playerList = playerList;
        timer.scheduleAtFixedRate(this.asTimerTaskClass(), 0, 500);
    }

    @Override
    public void notifyDrifting(ServerPlayer player, double angle, Entity vehicle, Vec3 velocity) { // TODO I think the responsibility of this class is to just manage when to send a packet for a player. So I think it is unnecessary to pass e.g driftAngle parameter just because I anyways will create another service that will calculate how much points the player has.
        long currentTime = System.currentTimeMillis();

        DriftData data = playerDriftData.computeIfAbsent(player.getUUID(), _ -> {
            var payload = new DriftStatePayload(DriftState.STARTED);
            ServerPlayNetworking.send(player, payload);
            return new DriftData(currentTime, 0);
        });

        data.lastDriftTime = currentTime;

        int oldScore = data.lastDriftScore;
        data.lastDriftScore += 100; // TODO some score system you know... UPD: nope, this class has responsibility to control which packet and when this packet is sent depending on situation

        var payload = new DriftPayload(oldScore, data.lastDriftScore);
        ServerPlayNetworking.send(player, payload);
    }

    @Override
    public void notifyCollision(ServerPlayer player, Entity vehicle) {
        playerDriftData.remove(player.getUUID());
        var payload = new DriftStatePayload(DriftState.FAILED);
        ServerPlayNetworking.send(player, payload);
    }

    @Override
    public void run() {
        checkDriftDelays();
    }

    private void checkDriftDelays() {
        final long currentTime = System.currentTimeMillis();
        Set<UUID> dataToRemove = new HashSet<>();

        playerDriftData.forEach((uuid, data) -> {
            var player = playerList.getPlayer(uuid);
            if (player == null) {
                return;
            }

            long driftDelay = currentTime - data.lastDriftTime;
            if (driftDelay > MAX_DRIFT_DELAY) {
                dataToRemove.add(uuid);
                ServerPlayNetworking.send(player, new DriftStatePayload(DriftState.ENDED));
            }
        });

        dataToRemove.forEach(playerDriftData::remove);
    }

    private static class DriftData {
        long lastDriftTime;
        int lastDriftScore;

        public DriftData(long lastDriftTime, int lastDriftScore) {
            this.lastDriftTime = lastDriftTime;
            this.lastDriftScore = lastDriftScore;
        }
    }
}

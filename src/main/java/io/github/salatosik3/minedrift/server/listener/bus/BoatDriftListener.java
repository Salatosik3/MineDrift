package io.github.salatosik3.minedrift.server.listener.bus;

import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class BoatDriftListener implements Consumer<BoatDriftEvent> {
    private final Map<UUID, Long> lastPacketSendTime = new HashMap<>();

    private long calcDelaySinceLastPacket(UUID uuid) {
        Long lastSendTime = lastPacketSendTime.get(uuid);
        if (lastSendTime == null) {
            lastPacketSendTime.put(uuid, System.currentTimeMillis());
            return -1;
        }
        long currentTime = System.currentTimeMillis();
        long delay = currentTime - lastSendTime;
        lastPacketSendTime.put(uuid, currentTime);
        return delay;
    }

    @Override
    public void accept(BoatDriftEvent boatDriftEvent) {
        var serverPlayer = boatDriftEvent.getServerPlayer();
        long delay = calcDelaySinceLastPacket(serverPlayer.getUUID());

        if (delay != -1 && delay < 500) {
            return;
        }

        var driftPayload = new DriftPayload(boatDriftEvent.getDriftAngle());
        ServerPlayNetworking.send(serverPlayer, driftPayload);
    }
}

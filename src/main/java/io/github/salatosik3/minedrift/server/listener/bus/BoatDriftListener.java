package io.github.salatosik3.minedrift.server.listener.bus;

import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import io.github.salatosik3.minedrift.server.service.DriftingPacketService;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class BoatDriftListener implements Consumer<BoatDriftEvent> {
    private final DriftingPacketService driftingPacketService;
    private final Map<UUID, Long> lastPacketSendTime = new HashMap<>();

    public BoatDriftListener(DriftingPacketService driftingPacketService) {
        this.driftingPacketService = driftingPacketService;
    }

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

        // TODO ummm, idk maybe I should delegate this type of work to the service in which the notify method is called below
        if (delay != -1 && delay < 500) {
            return;
        }

        driftingPacketService.notifyPlayerDrift(serverPlayer, boatDriftEvent.getDriftAngle());
    }
}

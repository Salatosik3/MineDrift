package io.github.salatosik3.minedrift.client.handler.packet;

import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class DriftPacketHandler implements ClientPlayNetworking.PlayPayloadHandler<DriftPayload> {

    private double driftAngle = 0;

    @Override
    public void receive(DriftPayload payload, ClientPlayNetworking.Context context) {
        driftAngle = payload.driftAngle();
    }

    public double getDriftAngle() {
        return driftAngle;
    }
}

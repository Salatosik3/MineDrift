package io.github.salatosik3.minedrift.client.handler.packet;

import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class DriftPacketHandler implements ServerPlayNetworking.PlayPayloadHandler<DriftPayload> {
    @Override
    public void receive(DriftPayload payload, ServerPlayNetworking.Context context) {

    }
}

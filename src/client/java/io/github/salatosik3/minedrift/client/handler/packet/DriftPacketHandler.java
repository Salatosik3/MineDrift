package io.github.salatosik3.minedrift.client.handler.packet;

import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class DriftPacketHandler implements ClientPlayNetworking.PlayPayloadHandler<DriftPayload>, DriftDataSource {

    @Override
    public void receive(DriftPayload payload, ClientPlayNetworking.Context context) {

    }

    @Override
    public int getPoints() {
        return 0;
    }
}

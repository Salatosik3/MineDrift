package io.github.salatosik3.minedrift.client.handler.packet;

import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class GenericPacketHandler <T> implements ClientPlayNetworking.PlayPayloadHandler<T> {
    @Override
    public void receive(T payload, ClientPlayNetworking.Context context) {

    }
}

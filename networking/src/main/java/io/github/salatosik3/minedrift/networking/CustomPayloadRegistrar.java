package io.github.salatosik3.minedrift.networking;

import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import io.github.salatosik3.minedrift.networking.client.DriftStatePayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class CustomPayloadRegistrar {
    public static final String MOD_ID = "minedrift";

    public static void registerAll() {
        var clientReg = PayloadTypeRegistry.clientboundPlay();
        clientReg.register(DriftPayload.TYPE, DriftPayload.CODEC);
        clientReg.register(DriftStatePayload.TYPE, DriftStatePayload.CODEC);
    }
}

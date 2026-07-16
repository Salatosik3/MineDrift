package io.github.salatosik3.minedrift.networking;

import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class Networking {
    public static final String MOD_ID = "minedrift";

    private static void init() {
        var clientReg = PayloadTypeRegistry.clientboundPlay();
        clientReg.register(DriftPayload.TYPE, DriftPayload.CODEC);
    }

    static {
        init();
    }
}

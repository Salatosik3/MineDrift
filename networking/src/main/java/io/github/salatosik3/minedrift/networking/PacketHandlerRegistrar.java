package io.github.salatosik3.minedrift.networking;

import io.github.salatosik3.minedrift.misc.Registrar;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PacketHandlerRegistrar {

    private static Registrar registrar;

    public static void registerAll() {

    }

    public static <T extends CustomPacketPayload, H extends ClientPlayNetworking.PlayPayloadHandler<T>> void register(H handler, CustomPacketPayload.Type<T> type) {
        registrar.register(handler.getClass(), type);
        ClientPlayNetworking.registerGlobalReceiver(type, handler);
    }

    public static <H> H get(Class<H> clazz) {
        return registrar.get(clazz);
    }
}

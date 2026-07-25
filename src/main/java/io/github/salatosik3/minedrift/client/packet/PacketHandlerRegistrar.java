package io.github.salatosik3.minedrift.client.packet;

import io.github.salatosik3.minedrift.misc.Registrar;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PacketHandlerRegistrar {

    private static Registrar registrar = new Registrar();

    public static <T extends CustomPacketPayload, H extends ClientPlayNetworking.PlayPayloadHandler<T>> void register(H handler, CustomPacketPayload.Type<T> type) {
        registrar.register(handler.getClass(), type);
        ClientPlayNetworking.registerGlobalReceiver(type, handler);
    }

    public static <H> H get(Class<H> clazz) {
        return registrar.get(clazz);
    }
}

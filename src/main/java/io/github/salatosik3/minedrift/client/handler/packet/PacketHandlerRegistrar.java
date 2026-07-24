package io.github.salatosik3.minedrift.client.handler.packet;

import io.github.salatosik3.minedrift.misc.Registrar;
import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Supplier;

public class PacketHandlerRegistrar {

    private static Registrar registrar;

    public static void registerAll() {
        register(DriftPacketHandler::new, DriftPayload.TYPE);
    }

    public static <T extends CustomPacketPayload,
            H extends ClientPlayNetworking.PlayPayloadHandler<T>> void register(Supplier<H> supplier, CustomPacketPayload.Type<T> type) {
        H instance = supplier.get();
        registrar.register(instance.getClass(), type);
        ClientPlayNetworking.registerGlobalReceiver(type, instance);
    }

    public static <H> H get(Class<H> clazz) {
        return registrar.get(clazz);
    }
}

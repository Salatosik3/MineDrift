package io.github.salatosik3.minedrift.networking.client;

import io.github.salatosik3.minedrift.networking.CustomPayloadRegistrar;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record DriftPayload(double driftAngle, int oldScore, int newScore) implements CustomPacketPayload {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(CustomPayloadRegistrar.MOD_ID, "drift");
    public static final CustomPacketPayload.Type<DriftPayload> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, DriftPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, DriftPayload::driftAngle,
            ByteBufCodecs.INT, DriftPayload::oldScore,
            ByteBufCodecs.INT, DriftPayload::newScore,
            DriftPayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
